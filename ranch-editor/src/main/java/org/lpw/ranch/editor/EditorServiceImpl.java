package org.lpw.ranch.editor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.async.AsyncService;
import org.lpw.ranch.editor.buy.BuyService;
import org.lpw.ranch.editor.download.DownloadService;
import org.lpw.ranch.editor.element.ElementModel;
import org.lpw.ranch.editor.element.ElementService;
import org.lpw.ranch.editor.file.FileService;
import org.lpw.ranch.editor.label.LabelModel;
import org.lpw.ranch.editor.label.LabelService;
import org.lpw.ranch.editor.role.RoleModel;
import org.lpw.ranch.editor.role.RoleService;
import org.lpw.ranch.editor.screenshot.ScreenshotService;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.popular.PopularService;
import org.lpw.ranch.push.helper.PushHelper;
import org.lpw.ranch.temporary.Temporary;
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
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.util.Zipper;
import org.lpw.tephra.wormhole.Protocol;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(EditorModel.NAME + ".service")
public class EditorServiceImpl implements EditorService, HourJob, DateJob {
    private static final String CACHE_SEARCH = EditorModel.NAME + ".search:";
    private static final String CACHE_SEARCH_LABEL = EditorModel.NAME + ".search.label:";
    private static final String CACHE_TEMPLATE = EditorModel.NAME + ".template:";

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
    private Context context;
    @Inject
    private Zipper zipper;
    @Inject
    private Logger logger;
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
    private Temporary temporary;
    @Inject
    private Pagination pagination;
    @Inject
    private LockHelper lockHelper;
    @Inject
    private AsyncService asyncService;
    @Inject
    private UserHelper userHelper;
    @Inject
    private PopularService popularService;
    @Inject
    private PushHelper pushHelper;
    @Inject
    private RoleService roleService;
    @Inject
    private LabelService labelService;
    @Inject
    private ElementService elementService;
    @Inject
    private ScreenshotService screenshotService;
    @Inject
    private FileService fileService;
    @Inject
    private DownloadService downloadService;
    @Inject
    private BuyService buyService;
    @Inject
    private Optional<Set<EditorPublishListener>> listeners;
    @Inject
    private EditorDao editorDao;
    @Value("${" + EditorModel.NAME + ".auto.pass:false}")
    private boolean autoPass;
    @Value("${" + EditorModel.NAME + ".auto.sale:false}")
    private boolean autoSale;
    @Value("${" + EditorModel.NAME + ".capture:}")
    private String capture;
    @Value("${" + EditorModel.NAME + ".capture.nomark:}")
    private String captureNomark;
    @Value("${" + EditorModel.NAME + ".capture.mark:}")
    private String captureMark;
    @Value("${" + EditorModel.NAME + ".capture.wait:5}")
    private int captureWait;
    private Map<String, String> random = new ConcurrentHashMap<>();

    @Override
    public JSONObject query(String user, String uid, String mobile, String email, String nick, int template, String type, String name,
                            String label, String group, int price, int vipPrice, int limitedPrice, int modified, String[] states,
                            String createStart, String createEnd, String modifyStart, String modifyEnd, Order order) {
        Set<String> ids = ids(user, uid, mobile, email, nick);
        if (ids != null && ids.isEmpty())
            return BeanFactory.getBean(PageList.class).setPage(0, 0, 0).toJson();

        return editorDao.query(ids, template, type, name, label, group, price, vipPrice, limitedPrice, null, modified,
                getStates(states), dateTime.getStart(createStart), dateTime.getEnd(createEnd), dateTime.getStart(modifyStart),
                dateTime.getEnd(modifyEnd), order, pagination.getPageSize(20), pagination.getPageNum()).toJson((editor, object) -> {
            RoleModel role = roleService.findOwner(editor.getId());
            if (role != null)
                object.put("owner", userHelper.get(role.getUser()));
            appendToJson(editor, object);
        });
    }

    private Set<String> ids(String user, String uid, String mobile, String email, String nick) {
        if (validator.isEmpty(user) && validator.isEmpty(uid) && validator.isEmpty(mobile)
                && validator.isEmpty(email) && validator.isEmpty(nick))
            return null;

        Set<String> set = validator.isEmpty(user) ? userHelper.ids(uid, null, null, nick, mobile, email, null,
                -1, -1, -1, null, null) : Collections.singleton(user);

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
        return editorDao.findById(id);
    }

    @Override
    public JSONObject find(String id) {
        return toJson(findById(id));
    }

    @Override
    public JSONObject templates(String[] ids) {
        JSONObject object = new JSONObject();
        if (validator.isEmpty(ids))
            return object;

        editorDao.templates(new HashSet<>(Arrays.asList(ids))).getList().forEach(editor -> object.put(editor.getId(), toJson(editor)));

        return object;
    }

    @Override
    public boolean notExistsGroup(String group) {
        return editorDao.findByGroup(group) == null;
    }

    @Override
    public boolean usable(String id) {
        String template = findTemplate(id);
        if (template == null)
            return false;

        EditorModel editor = findById(template);

        return editor.getPrice() == 0 || (userHelper.isVip() && editor.getVipPrice() == 0) || (editor.getLimitedPrice() == 0
                && editor.getLimitedTime() != null && editor.getLimitedTime().getTime() > System.currentTimeMillis())
                || buyService.find(userHelper.id(), template) != null;
    }

    @Override
    public boolean nomark(String id) {
        String template = findTemplate(id);
        if (template == null)
            return false;

        EditorModel editor = findById(template);
        if (editor.getTemplate() > 2)
            return true;

        return (userHelper.isVip() && editor.getVipPrice() == 0) || buyService.find(userHelper.id(), template) != null;
    }

    @Override
    public String findTemplate(String id) {
        String cacheKey = CACHE_TEMPLATE + id;
        String template = cache.get(cacheKey);
        if (!validator.isEmpty(template))
            return template;

        while (true) {
            if (validator.isEmpty(id))
                return null;

            EditorModel editor = findById(id);
            if (editor == null)
                return null;

            if (editor.getTemplate() > 0) {
                cache.put(cacheKey, editor.getId(), false);

                return editor.getId();
            }

            id = editor.getSource();
        }
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
        model.setKeyword(editor.getKeyword());
        model.setSummary(editor.getSummary());
        model.setWidth(editor.getWidth());
        model.setHeight(editor.getHeight());
        model.setImage(editor.getImage());
        if (!validator.isEmpty(editor.getScreenshot()))
            model.setScreenshot(editor.getScreenshot());
        model.setGroup(editor.getGroup());
        if (!validator.isEmpty(editor.getSource()))
            model.setSource(editor.getSource());
        model.setJson(editor.getJson());
        save(model, 0, null, true);

        return toJson(model);
    }

    @Override
    public JSONObject modify(EditorModel editor, int template) {
        EditorModel model = findById(editor.getId());
        if (template > -1)
            model.setTemplate(template);
        if (!validator.isEmpty(editor.getType()))
            model.setType(editor.getType());
        if (editor.getSort() > 0)
            model.setSort(editor.getSort());
        if (!validator.isEmpty(editor.getName()))
            model.setName(editor.getName());
        if (!validator.isEmpty(editor.getLabel()))
            model.setLabel(editor.getLabel());
        if (!validator.isEmpty(editor.getKeyword()))
            model.setKeyword(editor.getKeyword());
        if (!validator.isEmpty(editor.getSummary()))
            model.setSummary(editor.getSummary());
        if (editor.getWidth() > 0)
            model.setWidth(editor.getWidth());
        if (editor.getHeight() > 0)
            model.setHeight(editor.getHeight());
        if (!validator.isEmpty(editor.getImage()))
            model.setImage(editor.getImage());
        if (!validator.isEmpty(editor.getGroup()))
            model.setGroup(editor.getGroup());
        if (!validator.isEmpty(editor.getJson()))
            model.setJson(editor.getJson());
        save(model, model.getState(), null, false);

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
        List<ElementModel> elements = elementService.list(id);
        if (validator.isEmpty(elements))
            return "";

        EditorModel editor = findById(id);
        String sid = session.getId();

        return asyncService.submit(EditorModel.NAME + ".image", id, 20, () -> {
            File file = screenshotService.capture(sid, id, elements.get(0).getId(), editor.getWidth(), editor.getHeight());
            if (file == null)
                return "failure";

            EditorModel model = findById(id);
            model.setImage(wormholeHelper.image(null, null, null, file));
            save(model, 0, null, false);

            return "success";
        });
    }

    @Override
    public void image(String id, String image) {
        EditorModel editor = findById(id);
        if (editor == null)
            return;

        editor.setImage(image);
        save(editor, editor.getState(), null, false);
    }

    @Override
    public JSONObject state(String id, int state) {
        EditorModel editor = editorDao.findById(id);
        save(editor, state, null, false);

        return modelHelper.toJson(editor);
    }

    @Override
    public String pdf(String id, String email) {
        String sid = session.getId();
        String user = userHelper.id();
        boolean nomark = nomark(id);

        return asyncService.submit(EditorModel.NAME + ".pdf", id, 60, () -> {
            String path = pdf(sid, findById(id), nomark);
            String uri = path.substring(path.lastIndexOf(temporary.root()));
            downloadService.save(user, findTemplate(id), "pdf", uri, path);
            sendEmail(email, new File(path), user, EditorModel.NAME + ".pdf");

            return uri;
        });
    }

    private String pdf(String sid, EditorModel editor, boolean nomark) {
        String capture = nomark ? getCaptureNomark(sid, editor.getId()) : getCaptureMark(sid, editor.getId());
        if (capture == null)
            return "";

        return chromeHelper.pdf(capture, captureWait, editor.getWidth(), editor.getHeight(), "", temporary.root());
    }

    @Override
    public String images(String id, String email) {
        String sid = session.getId();
        String user = userHelper.id();
        boolean nomark = nomark(id);
        EditorModel editor = findById(id);
        List<ElementModel> elements = elementService.list(id);

        return asyncService.submit(EditorModel.NAME + ".images", id, 10 * elements.size(), () -> {
            List<File> list = screenshotService.capture(sid, editor, elements, nomark, false);
            if (list == null)
                return "";

            String uri = temporary.newSavePath(".zip");
            File file = new File(context.getAbsolutePath(uri));
            Map<String, File> map = new HashMap<>();
            for (int i = 0, size = list.size(); i < size; i++)
                map.put(i + ".jpeg", list.get(i));
            zipper.zip(map, file);
            downloadService.save(user, findTemplate(id), "pdf", uri, file.getAbsolutePath());
            sendEmail(email, file, user, EditorModel.NAME + ".images");

            return uri;
        });
    }

    private void sendEmail(String email, File file, String user, String key) {
        if (!validator.isEmpty(email))
            return;

        JSONObject args = new JSONObject();
        args.put("url", wormholeHelper.getUrl(Protocol.Https, wormholeHelper.file("temp", null, null, file), false));
        pushHelper.send(key, user, email, args);
    }

    @Override
    public JSONObject copy(String id, String type) {
        EditorModel editor = editorDao.findById(id);
        editor.setId(null);
        if (!validator.isEmpty(type))
            editor.setType(type);
        boolean fromTemplate = editor.getTemplate() > 0;
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

        editorDao.used(id);
        resetRandom(editor.getType());
    }

    private JSONObject toJson(EditorModel editor) {
        JSONObject object = modelHelper.toJson(editor);
        RoleModel role = roleService.find(userHelper.id(), editor.getId());
        if (role != null)
            object.put("role", role.getType());
        appendToJson(editor, object);

        return object;
    }

    private void appendToJson(EditorModel editor, JSONObject object) {
        object.put("files", fileService.query(editor.getId()));
    }

    @Override
    public void modify(Map<String, Long> map) {
        if (validator.isEmpty(map))
            return;

        map.forEach((id, modify) -> {
            EditorModel editor = findById(id);
            if (Math.abs(editor.getModify().getTime() - modify) > TimeUnit.Second.getTime())
                save(editor, editor.getState() == 3 ? editor.getState() : 0, new Timestamp(modify), false);
        });
    }

    @Override
    public void modify(String id) {
        EditorModel editor = findById(id);
        if (editor != null)
            save(editor, editor.getState() == 3 ? editor.getState() : 0, new Timestamp(System.currentTimeMillis()), false);
    }

    @Override
    public void labels(Map<String, StringBuilder> map) {
        Set<String> types = new HashSet<>();
        map.forEach((id, label) -> {
            EditorModel editor = findById(id);
            editor.setLabel(label.substring(1));
            editorDao.save(editor);
            types.add(editor.getType());
        });
        types.forEach(this::resetRandom);
    }

    @Override
    public void price(String[] ids, String type, String group, int price, int vipPrice, int limitedPrice, Timestamp limitedTime) {
        editorDao.price(ids, type, group, price, vipPrice, limitedPrice, limitedTime);
        resetRandom(type);
    }

    @Override
    public void group(String type, String oldGroup, String newGroup) {
        editorDao.group(type, oldGroup, newGroup);
    }

    @Override
    public void sort(String type, String[] ids, String[] sorts) {
        if (validator.isEmpty(ids) || validator.isEmpty(sorts) || ids.length != sorts.length)
            return;

        for (int i = 0; i < ids.length; i++)
            editorDao.sort(ids[i], type, numeric.toInt(sorts[i]));
        resetRandom(type);
    }

    @Override
    public void download(Map<String, Integer> map) {
        map.forEach((id, count) -> {
            if (count == 0)
                return;

            EditorModel editor = findById(id);
            if (editor == null || editor.getTemplate() == 0 || editor.getDownload() == count)
                return;

            editorDao.download(id, count);
        });
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
        boolean templatePassed = editor.getTemplate() > 0 && (editor.getState() == 3 || oldState == 3);
        editor.setModify(modify == null ? dateTime.now() : modify);
        editorDao.save(editor);
        if (owner)
            roleService.save(userHelper.id(), editor.getId(), RoleService.Type.Owner);
        roleService.modify(editor);
        if (editor.getTemplate() > 0)
            labelService.save(editor.getId(), editor.getLabel(), false);
        if (templatePassed)
            resetRandom(editor.getType());
    }

    private void autoState(EditorModel editor) {
        if (autoPass && editor.getState() == 0)
            editor.setState(1);
        if (autoSale && editor.getState() == 1)
            editor.setState(3);
    }

    @Override
    public String publish(String id, int width, int height) {
        String sid = session.getId();

        return asyncService.submit(EditorModel.NAME + ".publish", id, 600, () -> {
            EditorModel editor = findById(id);
            List<ElementModel> elements = elementService.list(id);
            screenshot(sid, editor, width, height);
            capture(sid, editor, elements, true);
            capture(sid, editor, elements, false);
            fileService.save(id, "pdf", new File(pdf(sid, editor, true)));
            fileService.save(id, "pdf.free", new File(pdf(sid, editor, false)));
            listeners.ifPresent(set -> set.forEach(listener -> listener.publish(sid, null, editor, elements)));

            return "";
        });
    }

    @Override
    public String publishes(String type, String[] types, int width, int height) {
        String sid = session.getId();

        return asyncService.submit(EditorModel.NAME + ".publishes", type, 24 * 60 * 60, () -> {
            Set<String> set = new HashSet<>();
            if (!validator.isEmpty(types)) {
                Collections.addAll(set, types);
                set.remove("");
            }

            editorDao.templates(type, 3).forEach(id -> {
                try {
                    EditorModel editor = findById(id);
                    List<ElementModel> elements = elementService.list(id);
                    if (set.isEmpty() || set.contains("screenshot"))
                        screenshot(sid, editor, width, height);
                    if (set.isEmpty() || set.contains("image"))
                        capture(sid, editor, elements, true);
                    if (set.isEmpty() || set.contains("image.free"))
                        capture(sid, editor, elements, false);
                    if (set.isEmpty() || set.contains("pdf"))
                        fileService.save(id, "pdf", new File(pdf(sid, editor, true)));
                    if (set.isEmpty() || set.contains("pdf.free"))
                        fileService.save(id, "pdf.free", new File(pdf(sid, editor, false)));
                    listeners.ifPresent(s -> s.forEach(listener -> listener.publish(sid, set, editor, elements)));
                    editorDao.close();
                } catch (Throwable throwable) {
                    logger.warn(throwable, "发布模板[{}:{}:{}:{}:{}]时发生异常！", type, Arrays.toString(types), width, height, id);
                }
            });
            resetRandom(type);

            return "";
        });
    }

    private void screenshot(String sid, EditorModel editor, int width, int height) {
        editor.setScreenshot(wormholeHelper.image(null, null, null,
                screenshotService.capture(sid, editor.getId(), "", width, height)));
        save(editor, editor.getState(), null, false);
    }

    private void capture(String sid, EditorModel editor, List<ElementModel> elements, boolean nomark) {
        List<File> list = screenshotService.capture(sid, editor, elements, nomark, nomark);
        Map<String, File> map = new HashMap<>();
        for (int i = 0, size = list.size(); i < size; i++)
            map.put(i + ".jpeg", list.get(i));
        File file = new File(context.getAbsolutePath(temporary.newSavePath(".zip")));
        try {
            zipper.zip(map, file);
            fileService.save(editor.getId(), nomark ? "image" : "image.free", file);
            io.delete(file);
        } catch (Throwable throwable) {
            logger.warn(throwable, "生成预览截图时发生异常！");
        }
    }

    @Override
    public JSONObject searchTemplate(String type, int template, String[] labels, String[] words, boolean free, boolean nofree, Order order) {
        int pageSize = pagination.getPageSize(20);
        if (validator.isEmpty(labels) && validator.isEmpty(words))
            return searchTemplate(type, template, free, nofree, order, pageSize);

        popular(type, template, true, labels);
        popular(type, template, false, words);
        String cacheKey = getSearchCacheKey(type, template, converter.toString(labels) + ":" + converter.toString(words)
                + ":" + free + ":" + nofree + ":" + order + ":" + pageSize + ":" + pagination.getPageNum());
        JSONObject object = cache.get(cacheKey);
        if (object != null)
            return object;

        Set<String> ids = null;
        List<String> lbs = searchWords(labels);
        if (!lbs.isEmpty()) {
            ids = labelService.query(lbs);
            if (validator.isEmpty(ids))
                return searchTemplateEmpty(cacheKey, pageSize);
        }

        List<String> wds = searchWords(words);
        if (!wds.isEmpty()) {
            List<String> list = luceneHelper.query(getLuceneKey(type, template), wds, true, 1024);
            if (list.isEmpty())
                return searchTemplateEmpty(cacheKey, pageSize);

            if (ids == null)
                ids = new HashSet<>(list);
            else {
                ids.retainAll(list);
                if (ids.isEmpty())
                    return searchTemplateEmpty(cacheKey, pageSize);
            }
        }

        object = editorDao.search(ids, template, type, free, nofree, order, pageSize, pagination.getPageNum()).toJson(this::appendToJson);
        cache.put(cacheKey, object, false);

        return object;
    }

    private List<String> searchWords(String[] words) {
        List<String> list = new ArrayList<>();
        if (!validator.isEmpty(words))
            for (String word : words)
                if (!validator.isEmpty(word))
                    list.add(word);

        return list;
    }

    private void popular(String type, int template, boolean label, String[] values) {
        popularService.increase(EditorModel.NAME + ":" + type + ":" + template + (label ? ":label" : ":word"), values);
    }

    private JSONObject searchTemplate(String type, int template, boolean free, boolean nofree, Order order, int pageSize) {
        String cacheKey = getSearchCacheKey(type, template, free + ":" + nofree + ":" + order
                + ":" + pageSize + ":" + pagination.getPageNum());
        JSONObject object = cache.get(cacheKey);
        if (object == null)
            cache.put(cacheKey, object = editorDao.search(null, template, type, free, nofree, order,
                    pageSize, pagination.getPageNum()).toJson(this::appendToJson), false);

        return object;
    }

    private JSONObject searchTemplateEmpty(String cacheKey, int pageSize) {
        JSONObject object = BeanFactory.getBean(PageList.class).setPage(0, pageSize, 0).toJson();
        cache.put(cacheKey, object, false);

        return object;
    }

    private String getSearchCacheKey(String type, int template, String key) {
        return random.computeIfAbsent(type, k -> CACHE_SEARCH + type + ":" + generator.random(32) + ":") + template + ":" + key;
    }

    @Override
    public String resetSearchIndex(String type, int template) {
        return asyncService.submit(EditorModel.NAME + ".reset-search-index", type + "." + template, 60 * 60, () -> {
            setSearchIndex(type, template);

            return "";
        });
    }

    private void resetRandom(String type) {
        random.remove(type);
    }

    @Override
    public JSONObject searchTemplate(String type, int template, String label, int size) {
        String cacheKey = CACHE_SEARCH_LABEL + type + ":" + template + ":" + label + ":" + size
                + ":" + System.currentTimeMillis() / TimeUnit.Day.getTime();
        JSONObject object = new JSONObject();
        JSONArray array = cache.get(cacheKey);
        object.put("refresh", array == null);
        if (array == null) {
            array = new JSONArray();
            List<LabelModel> labels = labelService.query(label);
            while (array.size() < size && !labels.isEmpty()) {
                EditorModel editor = findById(labels.remove(generator.random(0, labels.size() - 1)).getEditor());
                if (editor.getType().equals(type) && editor.getTemplate() == template && editor.getState() == 3)
                    array.add(modelHelper.toJson(editor));
            }
            cache.put(cacheKey, array, false);
        }
        object.put("list", array);

        return object;
    }

    @Override
    public String getCapture(String sid, String id) {
        return getCapture(capture, sid, id);
    }

    @Override
    public String getCaptureNomark(String sid, String id) {
        return getCapture(captureNomark, sid, id);
    }

    @Override
    public String getCaptureMark(String sid, String id) {
        return getCapture(captureMark, sid, id);
    }

    private String getCapture(String capture, String sid, String id) {
        if (validator.isEmpty(capture))
            return null;

        return capture + (capture.indexOf('?') == -1 ? "?" : "&") + "sid=" + sid + "&id=" + id;
    }

    @Override
    public int getCaptureWait() {
        return captureWait;
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
        });

        lockHelper.unlock(lockId);
    }

    @Override
    public void executeDateJob() {
        String lockId = lockHelper.lock(EditorModel.NAME + ".date", 100L, 3600);
        if (lockId == null)
            return;

        editorDao.typeTemplates().forEach((type, templates) -> templates.forEach(template -> setSearchIndex(type, template)));
        fileService.count().forEach(editorDao::download);
        buyService.count().forEach(editorDao::buy);
        lockHelper.unlock(lockId);
    }

    private void setSearchIndex(String type, int template) {
        String luceneKey = getLuceneKey(type, template);
        luceneHelper.clear(luceneKey);
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            PageList<EditorModel> pl = editorDao.query(template, type, 3, 20, i);
            pl.getList().forEach(editor -> {
                labelService.save(editor.getId(), editor.getLabel(), true);
                StringBuilder data = new StringBuilder().append(editor.getName()).append(',');
                append(data, editor.getLabel());
                append(data, editor.getKeyword());
                append(data, editor.getSummary());
                elementService.text(editor.getId(), data);
                luceneHelper.index(luceneKey, editor.getId(), data.toString());
            });
            if (logger.isInfoEnable())
                logger.info("添加[{}:{}]编辑器索引。", type, pl.getList().size());
            if (pl.getNumber() == pl.getPage())
                break;
        }
        resetRandom(type);
    }

    private void append(StringBuilder data, String string) {
        if (!validator.isEmpty(string))
            data.append(string).append(',');
    }

    private String getLuceneKey(String type, int template) {
        return EditorModel.NAME + "." + type + "." + template;
    }
}
