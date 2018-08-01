package org.lpw.ranch.editor.element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.editor.log.LogService;
import org.lpw.ranch.editor.role.RoleService;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
@Service(ElementModel.NAME + ".service")
public class ElementServiceImpl implements ElementService, MinuteJob {
    private static final String CACHE_MODEL = ElementModel.NAME + ".service.cache.model:";

    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Numeric numeric;
    @Inject
    private Json json;
    @Inject
    private Converter converter;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private EditorService editorService;
    @Inject
    private RoleService roleService;
    @Inject
    private LogService logService;
    @Inject
    private ElementDao elementDao;

    @Override
    public JSONArray query(String editor, String parent, boolean recursive) {
        JSONArray array = new JSONArray();
        elementDao.query(editor, validator.isEmpty(parent) ? editor : parent).getList().forEach(element -> {
            JSONObject object = modelHelper.toJson(element);
            if (recursive)
                object.put("children", query(editor, element.getId(), true));
            array.add(object);
        });

        return array;
    }

    @Override
    public List<ElementModel> list(String editor) {
        return elementDao.query(editor, editor).getList();
    }

    @Override
    public JSONObject find(String id, boolean recursive) {
        ElementModel element = findById(id);
        JSONObject object = modelHelper.toJson(element);
        if (recursive)
            object.put("children", query(element.getEditor(), element.getId(), true));

        return object;
    }

    @Override
    public ElementModel findById(String id) {
        String cacheKey = CACHE_MODEL + id;
        ElementModel element = cache.get(cacheKey);
        if (element == null)
            cache.put(cacheKey, element = elementDao.findById(id), false);

        return element;
    }

    @Override
    public JSONObject save(ElementModel element) {
        ElementModel model = validator.isEmpty(element.getId()) ? null : findById(element.getId());
        boolean isNew = model == null;
        if (isNew) {
            model = new ElementModel();
            model.setCreate(dateTime.now());
        }
        model.setEditor(element.getEditor());
        model.setParent(validator.isEmpty(element.getParent()) ? element.getEditor() : element.getParent());
        if (element.getSort() > 0)
            model.setSort(element.getSort());
        model.setJson(element.getJson());
        model.setText(element.getText());
        save(model, model.getSort());
        logService.save(model, isNew ? LogService.Operation.Create : LogService.Operation.Modify);

        return modelHelper.toJson(model);
    }

    @Override
    public JSONArray sort(String editor, String parent, String[] ids, String[] modifies) {
        JSONArray array = new JSONArray();
        if (ids.length > modifies.length)
            return array;

        if (validator.isEmpty(parent))
            parent = editor;
        for (int i = 0; i < ids.length; i++) {
            ElementModel element = findById(ids[i]);
            if (element == null || !element.getEditor().equals(editor) || !element.getParent().equals(parent)
                    || element.getSort() == i + 1 || element.getModify() != numeric.toLong(modifies[i]))
                continue;

            save(element, i + 1);
            JSONObject object = new JSONObject();
            object.put("id", element.getId());
            object.put("modify", element.getModify());
            array.add(object);
        }

        return array;
    }

    private void save(ElementModel element, int sort) {
        element.setSort(sort);
        element.setModify(System.currentTimeMillis());
        elementDao.save(element);
        cache.remove(CACHE_MODEL + element.getId());
    }

    @Override
    public void delete(String id) {
        ElementModel element = findById(id);
        delete(element);
        editorService.modify(element.getEditor());
    }

    @Override
    public void deletes(String editor) {
        elementDao.query(editor, editor).getList().forEach(this::delete);
        editorService.modify(editor);
    }

    private void delete(ElementModel element) {
        logService.save(element, LogService.Operation.Delete);
        elementDao.delete(element);
        cache.remove(CACHE_MODEL + element.getId());

        List<ElementModel> list = elementDao.query(element.getEditor(), element.getParent()).getList();
        for (int i = 0, size = list.size(); i < size; i++)
            save(list.get(i), i + 1);

        elementDao.query(element.getEditor(), element.getId()).getList().forEach(this::delete);
    }

    @Override
    public JSONArray batch(String parameters) {
        JSONArray array = new JSONArray();
        JSONArray params = json.toArray(parameters);
        if (params == null) {
            logger.warn(null, "批量操作参数集[{}]解析失败！", parameters);

            return array;
        }

        String editor = null;
        int size = params.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = params.getJSONObject(i);
            if (!object.containsKey("editor"))
                return array;

            if (i == 0) {
                editor = object.getString("editor");
                if (editorService.findById(editor) == null || !roleService.hasType(null, editor, RoleService.Type.Editor))
                    return array;
            } else if (!object.getString("editor").equals(editor))
                return array;

            switch (object.getString("operation")) {
                case "save":
                    if (parentNotExists(object.getString("parent"))
                            || modifyDisable(object.getString("id"), false, editor, object.getLongValue("modify")))
                        return array;
                    continue;
                case "sort":
                    if (parentNotExists(object.getString("parent")))
                        return array;
                    continue;
                case "delete":
                    if (modifyDisable(object.getString("id"), true, editor, object.getLongValue("modify")))
                        return array;
                    continue;
                default:
                    return array;
            }
        }

        for (int i = 0; i < size; i++) {
            JSONObject object = params.getJSONObject(i);
            switch (object.getString("operation")) {
                case "save":
                    Map<String, String> map = new HashMap<>();
                    object.keySet().stream().filter(key -> !key.equals("operation")).forEach(key -> map.put(key, object.getString(key)));
                    array.add(save(modelHelper.fromMap(map, ElementModel.class)));
                    continue;
                case "sort":
                    array.add(sort(editor, object.getString("parent"), converter.toArray(object.getString("ids"), ","),
                            converter.toArray(object.getString("modifies"), ",")));
                    continue;
                case "delete":
                    delete(object.getString("id"));
                    array.add("");
            }
        }

        return array;
    }

    private boolean parentNotExists(String parent) {
        return !validator.isEmpty(parent) && findById(parent) == null;
    }

    private boolean modifyDisable(String id, boolean exists, String editor, long modify) {
        if (validator.isEmpty(id))
            return exists;

        ElementModel element = findById(id);
        if (element == null)
            return exists;

        return !element.getEditor().equals(editor) || element.getModify() != modify;
    }

    @Override
    public void copy(String source, String target) {
        copy(source, target, source, target, dateTime.now(), System.currentTimeMillis());
    }

    private void copy(String sourceEditor, String targetEditor, String sourceParent, String targetParent, Timestamp create, long modify) {
        elementDao.query(sourceEditor, sourceParent).getList().forEach(element -> {
            String id = element.getId();
            element.setId(null);
            element.setEditor(targetEditor);
            element.setParent(targetParent);
            element.setCreate(create);
            element.setModify(modify);
            elementDao.save(element);
            logService.save(element, LogService.Operation.Create);
            copy(sourceEditor, targetEditor, id, element.getId(), create, modify);
        });
    }

    @Override
    public void text(String editor, StringBuilder data) {
        text(editor, editor, data);
    }

    private void text(String editor, String parent, StringBuilder data) {
        elementDao.query(editor, parent).getList().forEach(element -> {
            if (!validator.isEmpty(element.getText()))
                data.append(',').append(element.getText());
            text(editor, element.getId(), data);
        });
    }

    @Override
    public void executeMinuteJob() {
        editorService.modify(elementDao.modify(System.currentTimeMillis() - 3 * TimeUnit.Minute.getTime()));
    }
}
