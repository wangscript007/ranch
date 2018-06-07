package org.lpw.ranch.editor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.async.AsyncService;
import org.lpw.ranch.editor.element.ElementService;
import org.lpw.ranch.editor.role.RoleModel;
import org.lpw.ranch.editor.role.RoleService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.chrome.ChromeHelper;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
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
    private Io io;
    @Inject
    private ChromeHelper chromeHelper;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Session session;
    @Inject
    private WormholeHelper wormholeHelper;
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
    @Value("${" + EditorModel.NAME + ".image.url:}")
    private String imageUrl;

    @Override
    public EditorModel findById(String id) {
        String cacheKey = CACHE_MODEL + id;
        EditorModel editor = cache.get(cacheKey);
        if (editor == null)
            cache.put(cacheKey, editor = editorDao.findById(id), false);

        return editor;
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
        model.setType(editor.getType());
        model.setName(editor.getName());
        model.setKeyword(editor.getKeyword());
        model.setWidth(editor.getWidth());
        model.setHeight(editor.getHeight());
        model.setImage(editor.getImage());
        model.setJson(editor.getJson());
        save(editor, null, true);

        return toJson(model);
    }

    @Override
    public void image(String id) {
        if (validator.isEmpty(imageUrl))
            return;

        EditorModel editor = findById(id);
        asyncService.submit(EditorModel.NAME + "." + id, "", 20, () -> {
            String file = chromeHelper.jpeg(imageUrl + "?sid=" + session.getId() + "&id=" + id, 10,
                    0, 0, editor.getWidth(), editor.getHeight(), asyncService.root());
            if (validator.isEmpty(file))
                return "";

            EditorModel model = findById(id);
            model.setImage(wormholeHelper.image(null, null, null, new File(file)));
            save(model, null, false);

            return file;
        });
    }

    @Override
    public JSONObject copy(String id, String type) {
        EditorModel editor = findById(id);
        editor.setId(null);
        if (!validator.isEmpty(type))
            editor.setType(type);
        editor.setCreate(dateTime.now());
        save(editor, null, true);

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
                save(editor, new Timestamp(modify), false);
        });
    }

    private void save(EditorModel editor, Timestamp modify, boolean owner) {
        editor.setModify(modify == null ? dateTime.now() : modify);
        editorDao.save(editor);
        if (owner)
            roleService.save(userHelper.id(), editor.getId(), RoleService.Type.Owner);
        roleService.modify(editor.getId(), editor.getModify());
        cache.remove(CACHE_MODEL + editor.getId());
    }
}
