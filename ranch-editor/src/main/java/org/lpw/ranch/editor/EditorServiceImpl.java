package org.lpw.ranch.editor;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.async.AsyncService;
import org.lpw.ranch.editor.element.ElementModel;
import org.lpw.ranch.editor.element.ElementService;
import org.lpw.ranch.editor.role.RoleModel;
import org.lpw.ranch.editor.role.RoleService;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.chrome.ChromeHelper;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.lucene.LuceneHelper;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.scheduler.HourJob;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(EditorModel.NAME + ".service")
public class EditorServiceImpl implements EditorService, HourJob, DateJob {
    private static final String CACHE_MODEL = EditorModel.NAME + ".service.cache.model:";
    private static final String CACHE_QUERY = EditorModel.NAME + ".service.cache.query:";

    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private Numeric numeric;
    @Inject
    private DateTime dateTime;
    @Inject
    private Io io;
    @Inject
    private Generator generator;
    @Inject
    private Converter converter;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Session session;
    @Inject
    private ChromeHelper chromeHelper;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private LuceneHelper luceneHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private LockHelper lockHelper;
    @Inject
    private AsyncService asyncService;
    @Inject
    private UserHelper userHelper;
    @Inject
    private RoleService roleService;
    @Inject
    private ElementService elementService;
    @Inject
    private EditorDao editorDao;
    @Value("${" + EditorModel.NAME + ".auto.pass:false}")
    private boolean autoPass;
    @Value("${" + EditorModel.NAME + ".auto.sale:false}")
    private boolean autoSale;
    @Value("${" + EditorModel.NAME + ".image:}")
    private String image;
    @Value("${" + EditorModel.NAME + ".pdf.ordinary:}")
    private String pdfOrdinary;
    @Value("${" + EditorModel.NAME + ".pdf.vip:}")
    private String pdfVip;
    @Value("${" + EditorModel.NAME + ".template-types:}")
    private String templateTypes;
    private Map<String, String> random = new ConcurrentHashMap<>();
    private Set<Integer> onsaleState = Collections.singleton(3);

    @Override
    public JSONObject query(String uid, String mobile, String email, String nick, int template, String type, String name, String label,
                            int modified, String[] states, String createStart, String createEnd, String modifyStart, String modifyEnd,
                            Order order) {
        Set<String> ids = ids(uid, mobile, email, nick);
        if (ids != null && ids.isEmpty())
            return BeanFactory.getBean(PageList.class).setPage(0, 0, 0).toJson();

        return editorDao.query(ids, template, type, name, label, modified, getStates(states), dateTime.getStart(createStart),
                dateTime.getEnd(createEnd), dateTime.getStart(modifyStart), dateTime.getEnd(modifyEnd), order,
                pagination.getPageSize(20), pagination.getPageNum()).toJson((editor, object) -> {
            RoleModel role = roleService.findOwner(editor.getId());
            if (role != null)
                object.put("owner", userHelper.get(role.getUser()));
        });
    }

    private Set<String> ids(String uid, String mobile, String email, String nick) {
        if (validator.isEmpty(uid) && validator.isEmpty(mobile) && validator.isEmpty(email) && validator.isEmpty(nick))
            return null;

        Set<String> set = userHelper.ids(uid, null, null, nick, mobile, email, null,
                -1, -1, -1, null, null);

        return set.isEmpty() ? set : roleService.editors(set);
    }

    @Override
    public JSONObject user(int template, String type, String[] states) {
        return roleService.query(userHelper.id(), template, type, getStates(states)).toJson(role -> this.find(role.getEditor()));
    }

    private Set<Integer> getStates(String[] states) {
        Set<Integer> set = new HashSet<>();
        if (!validator.isEmpty(states))
            for (String state : states)
                if (!validator.isEmpty(state))
                    set.add(numeric.toInt(state));

        return set;
    }

    @Override
    public EditorModel findById(String id) {
        String cacheKey = CACHE_MODEL + id;
        EditorModel editor = cache.get(cacheKey);
        if (editor == null)
            cache.put(cacheKey, editor = editorDao.findById(id), false);

        return editor;
    }

    @Override
    public JSONObject find(String id) {
        return toJson(findById(id));
    }

    @Override
    public JSONObject save(EditorModel editor) {
        EditorModel model = validator.isEmpty(editor.getId()) ? null : findById(editor.getId());
        if (model == null) {
            model = new EditorModel();
            model.setCreate(dateTime.now());
        }
        model.setTemplate(editor.getTemplate());
        model.setType(editor.getType());
        model.setSort(editor.getSort());
        model.setName(editor.getName());
        model.setLabel(editor.getLabel());
        model.setWidth(editor.getWidth());
        model.setHeight(editor.getHeight());
        model.setImage(editor.getImage());
        model.setJson(editor.getJson());
        save(model, 0, null, true);

        return toJson(model);
    }

    @Override
    public JSONObject modify(EditorModel editor, int template) {
        EditorModel model = findById(editor.getId());
        if (template > 0)
            model.setTemplate(template);
        if (!validator.isEmpty(editor.getType()))
            model.setType(editor.getType());
        if (editor.getSort() > 0)
            model.setSort(editor.getSort());
        if (!validator.isEmpty(editor.getName()))
            model.setName(editor.getName());
        if (!validator.isEmpty(editor.getLabel()))
            model.setLabel(editor.getLabel());
        if (editor.getWidth() > 0)
            model.setWidth(editor.getWidth());
        if (editor.getHeight() > 0)
            model.setHeight(editor.getHeight());
        if (!validator.isEmpty(editor.getImage()))
            model.setImage(editor.getImage());
        if (!validator.isEmpty(editor.getJson()))
            model.setJson(editor.getJson());
        save(model, 0, null, false);

        return toJson(model);
    }

    @Override
    public JSONObject name(String id, String name) {
        EditorModel editor = findById(id);
        editor.setName(name);
        save(editor, 0, null, false);

        return toJson(editor);
    }

    @Override
    public String image(String id) {
        if (validator.isEmpty(image))
            return "";

        EditorModel editor = findById(id);
        String sid = session.getId();

        return asyncService.submit(EditorModel.NAME + ".image." + id, "", 20, () -> {
            String file = chromeHelper.jpeg(image + "?sid=" + sid + "&id=" + id, 10,
                    0, 0, editor.getWidth(), editor.getHeight(), 100, asyncService.root());
            if (validator.isEmpty(file))
                return "";

            EditorModel model = findById(id);
            model.setImage(wormholeHelper.image(null, null, null, new File(file)));
            save(model, 0, null, false);

            return file;
        });
    }

    @Override
    public void screenshot(String id, String uri) {
        EditorModel editor = editorDao.findById(id);
        editor.setScreenshot(uri);
        save(editor, editor.getState(), null, false);
    }

    @Override
    public JSONObject state(String id, int state) {
        EditorModel editor = editorDao.findById(id);
        save(editor, state, null, false);

        return modelHelper.toJson(editor);
    }

    @Override
    public String pdf(String id) {
        StringBuilder pdf = new StringBuilder(userHelper.isVip() ? pdfVip : pdfOrdinary);
        if (validator.isEmpty(pdf))
            return "";

        EditorModel editor = findById(id);
        pdf.append(pdf.indexOf("?") == -1 ? '?' : '&').append("sid=").append(session.getId()).append("&id=").append(id);

        return asyncService.submit(EditorModel.NAME + ".pdf." + id, "", 60, () -> {
            String path = chromeHelper.pdf(pdf.toString(), 30,
                    editor.getWidth(), editor.getHeight(), "", asyncService.root());

            return path.substring(path.lastIndexOf(asyncService.root()));
        });
    }

    @Override
    public JSONObject copy(String id, String type) {
        EditorModel editor = editorDao.findById(id);
        editor.setId(null);
        if (!validator.isEmpty(type))
            editor.setType(type);
        boolean fromTemplate = editor.getTemplate() == 1;
        editor.setSource(fromTemplate ? id : editor.getSource());
        editor.setTemplate(0);
        if (fromTemplate)
            editor.setModified(0);
        editor.setUsed(0);
        editor.setCreate(dateTime.now());
        save(editor, 0, null, true);
        elementService.copy(id, editor.getId());
        used(editor.getSource());

        return toJson(editor);
    }

    private void used(String id) {
        if (validator.isEmpty(id))
            return;

        EditorModel editor = findById(id);
        if (editor == null)
            return;

        editor.setUsed(editor.getUsed() + 1);
        save(editor, editor.getState(), editor.getModify(), false);
    }

    private JSONObject toJson(EditorModel editor) {
        JSONObject object = modelHelper.toJson(editor);
        RoleModel role = roleService.find(userHelper.id(), editor.getId());
        if (role == null)
            return object;

        object.put("role", role.getType());

        return object;
    }

    @Override
    public void modify(Map<String, Long> map) {
        if (validator.isEmpty(map))
            return;

        map.forEach((id, modify) -> {
            EditorModel editor = findById(id);
            if (Math.abs(editor.getModify().getTime() - modify) > TimeUnit.Second.getTime())
                save(editor, 0, new Timestamp(modify), false);
        });
    }

    @Override
    public void modify(String id) {
        EditorModel editor = findById(id);
        if (editor != null)
            save(editor, 0, new Timestamp(System.currentTimeMillis()), false);
    }

    @Override
    public void delete(String id) {
        EditorModel editor = findById(id);
        if (editor != null)
            save(editor, 5, new Timestamp(System.currentTimeMillis()), false);
    }

    @Override
    public void restore(String id) {
        EditorModel editor = findById(id);
        if (editor != null)
            save(editor, 0, new Timestamp(System.currentTimeMillis()), true);
    }

    private void save(EditorModel editor, int state, Timestamp modify, boolean owner) {
        int oldState = editor.getState();
        editor.setState(state);
        autoState(editor);
        boolean resetRandom = editor.getTemplate() == 1 && (editor.getState() == 3 || oldState == 3);
        editor.setModify(modify == null ? dateTime.now() : modify);
        editorDao.save(editor);
        if (owner)
            roleService.save(userHelper.id(), editor.getId(), RoleService.Type.Owner);
        roleService.modify(editor);
        cache.remove(CACHE_MODEL + editor.getId());
        if (resetRandom)
            resetRandom(editor.getType());
    }

    private void autoState(EditorModel editor) {
        if (autoPass && editor.getState() == 0)
            editor.setState(1);
        if (autoSale && editor.getState() == 1)
            editor.setState(3);
    }

    @Override
    public JSONObject searchTemplate(String type, String[] labels, String[] words, Order order) {
        List<String> list = searchWords(labels);

        return list.isEmpty() ? searchTemplate(type, "", searchWords(words), order)
                : searchTemplate(type, ".label", list, order);
    }

    private List<String> searchWords(String[] words) {
        List<String> list = new ArrayList<>();
        if (!validator.isEmpty(words))
            for (String word : words)
                if (!validator.isEmpty(word))
                    list.add(word);

        return list;
    }

    private JSONObject searchTemplate(String type, String suffix, List<String> list, Order order) {
        int pageSize = pagination.getPageSize(20);
        if (list.isEmpty())
            return searchTemplate(type, order, pageSize);

        String cacheKey = getSearchCacheKey(type, suffix + ":" + converter.toString(list) + ":" + order
                + ":" + pageSize + ":" + pagination.getPageNum());
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            List<String> ids = luceneHelper.query(getLuceneKey(type) + suffix, list, true, 1024);
            if (ids.isEmpty())
                object = BeanFactory.getBean(PageList.class).setPage(0, pageSize, 0).toJson();
            else
                object = editorDao.query(new HashSet<>(ids), 1, type, null, null, -1, onsaleState, null,
                        null, null, null, order, pageSize, pagination.getPageNum()).toJson();
            cache.put(cacheKey, object, false);
        }

        return object;
    }

    private JSONObject searchTemplate(String type, Order order, int pageSize) {
        String cacheKey = getSearchCacheKey(type, order + ":" + pageSize + ":" + pagination.getPageNum());
        JSONObject object = cache.get(cacheKey);
        if (object == null)
            cache.put(cacheKey, object = editorDao.query(null, 1, type, null, null, -1, onsaleState,
                    null, null, null, null, order, pageSize,
                    pagination.getPageNum()).toJson(), false);

        return object;
    }

    private String getSearchCacheKey(String type, String key) {
        return random.computeIfAbsent(type, k -> CACHE_QUERY + type + ":" + generator.random(32) + ":") + key;
    }

    @Override
    public String resetSearchIndex(String type) {
        if (!templateTypes.contains(type))
            return "";

        return asyncService.submit(EditorModel.NAME + ".reset-search-index", type, 60 * 60, () -> {
            setSearchIndex(type);

            return "";
        });
    }

    private void resetRandom(String type) {
        random.remove(type);
    }

    @Override
    public void executeHourJob() {
        String lockId = lockHelper.lock(EditorModel.NAME + ".hour", 100L, 3600);
        if (lockId == null)
            return;

        editorDao.query(new Timestamp[]{new Timestamp(System.currentTimeMillis() - (TimeUnit.Hour.getTime() << 1)),
                new Timestamp(System.currentTimeMillis() - (TimeUnit.Hour.getTime() >> 1))}).getList().forEach(editor -> {
            int[] count = elementService.count(editor.getId());
            editor.setTotal(count[0]);
            editor.setModified(count[1]);
            editorDao.save(editor);
            cache.remove(CACHE_MODEL + editor.getId());
        });

        lockHelper.unlock(lockId);
    }

    @Override
    public void executeDateJob() {
        if (validator.isEmpty(templateTypes))
            return;

        for (String type : converter.toArray(templateTypes, ","))
            setSearchIndex(type);
    }

    private void setSearchIndex(String type) {
        String luceneKey = getLuceneKey(type);
        luceneHelper.clear(luceneKey);
        String labelLuceneKey = luceneKey + ".label";
        luceneHelper.clear(labelLuceneKey);
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            PageList<EditorModel> pl = editorDao.query(null, 1, type, null, null, -1, onsaleState,
                    null, null, null, null, Order.None, 20, i);
            pl.getList().forEach(editor -> {
                StringBuilder data = new StringBuilder().append(editor.getName()).append(',').append(editor.getLabel());
                elementService.text(editor.getId(), data);
                luceneHelper.source(luceneKey, editor.getId(), data.toString());
                luceneHelper.source(labelLuceneKey, editor.getId(), editor.getLabel());
            });
            if (pl.getNumber() == pl.getPage())
                break;
        }
        luceneHelper.index(luceneKey);
        luceneHelper.index(labelLuceneKey);
        resetRandom(type);
    }

    private String getLuceneKey(String type) {
        return EditorModel.NAME + "." + type;
    }
}
