package org.lpw.ranch.editor.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorModel;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lpw
 */
@Service(RoleModel.NAME + ".service")
public class RoleServiceImpl implements RoleService {
    private static final String CACHE_USER_EDITOR = RoleModel.NAME + ".service.cache.user-editor:";

    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Generator generator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private EditorService editorService;
    @Inject
    private RoleDao roleDao;

    @Override
    public PageList<RoleModel> query(String user, int template, String etype, Set<Integer> states) {
        return roleDao.query(user, template, etype, states, pagination.getPageSize(20), pagination.getPageNum());
    }

    @Override
    public JSONArray query(String editor) {
        return modelHelper.toJson(roleDao.query(editor, true).getList());
    }

    @Override
    public RoleModel findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public RoleModel find(String user, String editor) {
        String cacheKey = getCacheKey(user, editor);
        RoleModel role = cache.get(cacheKey);
        if (role == null)
            cache.put(cacheKey, role = roleDao.find(user, editor), false);

        return role;
    }

    @Override
    public Set<String> editors(Set<String> users) {
        Set<String> set = new HashSet<>();
        roleDao.query(users).getList().forEach(role -> set.add(role.getEditor()));

        return set;
    }

    @Override
    public boolean hasType(String user, String editorId, Type type) {
        EditorModel editor = editorService.findById(editorId);
        if (editor.getTemplate() == 1 && editor.getState() == 3 && type == Type.Viewer)
            return true;

        if (validator.isEmpty(user))
            user = userHelper.id();
        if (validator.isEmpty(user))
            return false;

        RoleModel role = find(user, editorId);
        if (role != null && role.getType() <= type.ordinal())
            return true;

        if (userHelper.get(user).getIntValue("grade") < 50)
            return false;

        return editor.getTemplate() == 1 || type == Type.Viewer;
    }

    @Override
    public JSONObject countOwner() {
        JSONObject object = new JSONObject();
        String user = userHelper.id();
        object.put("total", roleDao.count(user, Type.Owner.ordinal()));
        object.put("recycle", roleDao.count(user, Type.Owner.ordinal(), 5));

        return object;
    }

    @Override
    public void save(String user, String editor, Type type) {
        save(user, editor, type, null);
    }

    @Override
    public JSONObject share(String editor, String password) {
        return modelHelper.toJson(save(generator.random(36), editor, Type.Viewer, password));
    }

    @Override
    public void password(String id, String password) {
        RoleModel role = roleDao.findById(id);
        role.setPassword(password);
        save(role);
    }

    private RoleModel save(String user, String editor, Type type, String password) {
        RoleModel role = find(user, editor);
        if (role == null) {
            role = new RoleModel();
            role.setUser(user);
            role.setEditor(editor);
            role.setCreate(dateTime.now());
        }
        role.setType(type.ordinal());
        if (!validator.isEmpty(password))
            role.setPassword(password);
        save(role);

        return role;
    }

    @Override
    public void modify(EditorModel editor) {
        roleDao.query(editor.getId(), false).getList().forEach(role -> {
            role.setTemplate(editor.getTemplate());
            role.setEtype(editor.getType());
            role.setState(editor.getState());
            role.setModify(editor.getModify());
            save(role);
        });
    }

    private void save(RoleModel role) {
        roleDao.save(role);
        cache.remove(getCacheKey(role.getUser(), role.getEditor()));
    }

    @Override
    public void delete(String user, String editor) {
        if (validator.isEmpty(user))
            user = userHelper.id();
        RoleModel role = find(user, editor);
        if (role.getType() >= Type.Editor.ordinal())
            delete(role);
        else {
            roleDao.query(editor, false).getList().stream()
                    .filter(r -> r.getType() >= Type.Editor.ordinal()).forEach(this::delete);
            editorService.delete(editor);
        }
    }

    private void delete(RoleModel role) {
        roleDao.delete(role);
        cache.remove(getCacheKey(role.getUser(), role.getEditor()));
    }

    private String getCacheKey(String user, String editor) {
        return CACHE_USER_EDITOR + user + ":" + editor;
    }
}
