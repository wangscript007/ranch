package org.lpw.ranch.editor;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.element.ElementService;
import org.lpw.ranch.editor.role.RoleService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author lpw
 */
@Service(EditorModel.NAME + ".service")
public class EditorServiceImpl implements EditorService {
    private static final String CACHE_MODEL = EditorModel.NAME + ".service.cache.model:";

    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private RoleService roleService;
    @Inject
    private ElementService elementService;
    @Inject
    private EditorDao editorDao;

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
        return modelHelper.toJson(findById(id));
    }

    @Override
    public JSONObject save(EditorModel editor) {
        EditorModel model = validator.isEmpty(editor.getId()) ? null : findById(editor.getId());
        if (model == null) {
            model = new EditorModel();
            model.setCreate(dateTime.now());
        }
        model.setType(editor.getType());
        model.setName(editor.getName());
        model.setWidth(editor.getWidth());
        model.setHeight(editor.getHeight());
        model.setImage(editor.getImage());
        model.setJson(editor.getJson());
        model.setModify(dateTime.now());
        editorDao.save(model);
        roleService.save(userHelper.id(), model.getId(), RoleService.Type.Owner);
        cache.remove(CACHE_MODEL + model.getId());

        return modelHelper.toJson(model);
    }

    @Override
    public JSONObject copy(String id, String type) {
        EditorModel editor = findById(id);
        editor.setId(null);
        if (!validator.isEmpty(type))
            editor.setType(type);
        editor.setCreate(dateTime.now());
        editor.setModify(dateTime.now());
        editorDao.save(editor);
        elementService.copy(id, editor.getId());

        return modelHelper.toJson(editor);
    }

    @Override
    public void modify(Map<String, Timestamp> map) {
        if (validator.isEmpty(map))
            return;

        map.forEach((id, modify) -> {
            EditorModel editor = findById(id);
            if (Math.abs(editor.getModify().getTime() - modify.getTime()) < TimeUnit.Second.getTime())
                return;

            editor.setModify(modify);
            editorDao.save(editor);
            cache.remove(CACHE_MODEL + id);
        });
    }
}
