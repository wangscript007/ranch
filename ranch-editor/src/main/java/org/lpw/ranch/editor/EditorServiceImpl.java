package org.lpw.ranch.editor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.async.AsyncService;
import org.lpw.ranch.editor.element.ElementService;
import org.lpw.ranch.editor.role.RoleModel;
import org.lpw.ranch.editor.role.RoleService;
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
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(EditorModel.NAME + ".service")
public class EditorServiceImpl implements EditorService, DateJob {
    private static final String CACHE_MODEL = EditorModel.NAME + ".service.cache.model:";
    private static final String CACHE_QUERY = EditorModel.NAME + ".service.cache.query:";

    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
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
    @Value("${" + EditorModel.NAME + ".pdf:}")
    private String pdf;
    @Value("${" + EditorModel.NAME + ".template-types:}")
    private String templateTypes;
    private Map<String, String> random = new ConcurrentHashMap<>();

    @Override
    public JSONObject query(String mobile, String email, String nick, int template, String type, String name, String keyword, int state,
                            String createStart, String createEnd, String modifyStart, String modifyEnd) {
        return editorDao.query(roleService.editors(userHelper.ids(null, null, nick, mobile, email,
                null, -1, -1, -1, null, null)), template,
                type, name, keyword, state, dateTime.getStart(createStart), dateTime.getEnd(createEnd), dateTime.getStart(modifyStart),
                dateTime.getEnd(modifyEnd), pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject queryUser() {
        PageList<RoleModel> roles = roleService.query(userHelper.id());
        JSONArray list = new JSONArray();
        roles.getList().forEach(role -> list.add(find(role.getEditor())));
        JSONObject object = roles.toJson(false);
        object.put("list", list);

        return object;
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
        model.setName(editor.getName());
        model.setKeyword(editor.getKeyword());
        model.setWidth(editor.getWidth());
        model.setHeight(editor.getHeight());
        model.setImage(editor.getImage());
        model.setJson(editor.getJson());
        save(model, 0, null, true);

        return toJson(model);
    }

    @Override
    public String image(String id) {
        if (validator.isEmpty(image))
            return "";

        EditorModel editor = findById(id);
        String sid = session.getId();

        return asyncService.submit(EditorModel.NAME + ".image." + id, "", 20, () -> {
            String file = chromeHelper.jpeg(image + "?sid=" + sid + "&id=" + id, 10,
                    0, 0, editor.getWidth(), editor.getHeight(), asyncService.root());
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
        save(editor, 0, null, false);
    }

    @Override
    public JSONObject state(String id, int state) {
        EditorModel editor = editorDao.findById(id);
        save(editor, state, null, false);

        return modelHelper.toJson(editor);
    }

    @Override
    public String pdf(String id) {
        if (validator.isEmpty(pdf))
            return "";

        EditorModel editor = findById(id);
        String sid = session.getId();

        return asyncService.submit(EditorModel.NAME + ".pdf." + id, "", 60, () -> {
            String path = chromeHelper.pdf(pdf + "?sid=" + sid + "&id=" + id, 30,
                    editor.getWidth(), editor.getHeight(), "", asyncService.root());

            return path.substring(path.lastIndexOf(asyncService.root()));
        });
    }

    @Override
    public JSONObject copy(String id, String type) {
        EditorModel editor = findById(id);
        editor.setId(null);
        if (!validator.isEmpty(type))
            editor.setType(type);
        editor.setTemplate(0);
        editor.setCreate(dateTime.now());
        save(editor, 0, null, true);
        elementService.copy(id, editor.getId());

        return toJson(editor);
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
        if (editor == null)
            return;

        save(editor, 0, new Timestamp(System.currentTimeMillis()), false);
    }

    @Override
    public void delete(String id) {
        EditorModel editor = findById(id);
        if (editor == null)
            return;

        save(editor, 5, new Timestamp(System.currentTimeMillis()), false);
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
        roleService.modify(editor.getId(), editor.getModify());
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

    @SuppressWarnings({"unchecked"})
    @Override
    public JSONObject searchTemplate(String type, String[] words) {
        int pageSize = pagination.getPageSize(20);
        if (validator.isEmpty(words))
            return searchTemplate(type, pageSize);

        Set<String> set = new HashSet<>(Arrays.asList(words));
        String cacheKey = getSearchCacheKey(type, converter.toString(set) + ":" + pageSize + ":" + pagination.getPageNum());
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            Set<String> ids = luceneHelper.query(getLuceneKey(type), set, 1024);
            if (ids.isEmpty()) {
                PageList<EditorModel> pl = BeanFactory.getBean(PageList.class);
                pl.setPage(0, pageSize, 0);
                object = pl.toJson();
            } else {
                object = editorDao.query(ids, 1, type, null, null, 3, null,
                        null, null, null, pageSize, pagination.getPageNum()).toJson();
            }
            cache.put(cacheKey, object, false);
        }

        return object;
    }

    private JSONObject searchTemplate(String type, int pageSize) {
        String cacheKey = getSearchCacheKey(type, ":" + pageSize + ":" + pagination.getPageNum());
        JSONObject object = cache.get(cacheKey);
        if (object == null)
            cache.put(cacheKey, object = editorDao.query(null, 1, type, null, null, 3,
                    null, null, null, null, pageSize,
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
    public void executeDateJob() {
        if (validator.isEmpty(templateTypes))
            return;

        for (String type : converter.toArray(templateTypes, ","))
            setSearchIndex(type);
    }

    private void setSearchIndex(String type) {
        String luceneKey = getLuceneKey(type);
        luceneHelper.clear(luceneKey);
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            PageList<EditorModel> pl = editorDao.query(null, 1, type, null, null, 3,
                    null, null, null, null, 20, i);
            pl.getList().forEach(editor -> {
                StringBuilder data = new StringBuilder().append(editor.getName()).append(',').append(editor.getKeyword());
                elementService.text(editor.getId(), data);
                luceneHelper.source(luceneKey, editor.getId(), data.toString());
            });
            if (pl.getNumber() == pl.getPage())
                break;
        }
        luceneHelper.index(luceneKey);
        resetRandom(type);
    }

    private String getLuceneKey(String type) {
        return EditorModel.NAME + "." + type;
    }
}
