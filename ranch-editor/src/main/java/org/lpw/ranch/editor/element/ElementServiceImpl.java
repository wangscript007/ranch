package org.lpw.ranch.editor.element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.editor.log.LogService;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;

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
    private ModelHelper modelHelper;
    @Inject
    private EditorService editorService;
    @Inject
    private LogService logService;
    @Inject
    private ElementDao elementDao;

    @Override
    public JSONArray query(String editor, String parent, boolean recursive) {
        JSONArray array = new JSONArray();
        elementDao.query(editor, validator.isEmpty(parent) ? editor : parent).getList().forEach(elemnt -> {
            JSONObject object = modelHelper.toJson(elemnt);
            if (recursive)
                object.put("children", query(editor, elemnt.getId(), true));
            array.add(object);
        });

        return array;
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
        model.setSort(element.getSort());
        model.setType(element.getType());
        model.setX(element.getX());
        model.setY(element.getY());
        model.setWidth(element.getWidth());
        model.setHeight(element.getHeight());
        model.setJson(element.getJson());
        save(model, element.getSort());
        logService.save(model, isNew ? LogService.Operation.Create : LogService.Operation.Modify);

        return modelHelper.toJson(model);
    }

    @Override
    public void sort(String editor, String parent, String[] ids) {
        if (validator.isEmpty(parent))
            parent = editor;
        for (int i = 0; i < ids.length; i++) {
            ElementModel element = findById(ids[i]);
            if (element == null || !element.getEditor().equals(editor) || !element.getParent().equals(parent) || element.getSort() == i)
                continue;

            save(element, i);
        }
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
        logService.save(element, LogService.Operation.Delete);
        elementDao.delete(element);
        cache.remove(CACHE_MODEL + element.getId());
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
    public void executeMinuteJob() {
        editorService.modify(elementDao.modify(new Timestamp(System.currentTimeMillis() - 3 * TimeUnit.Minute.getTime())));
    }
}
