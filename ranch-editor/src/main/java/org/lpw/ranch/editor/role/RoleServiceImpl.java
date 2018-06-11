package org.lpw.ranch.editor.role;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;
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
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private RoleDao roleDao;

    @Override
    public PageList<RoleModel> query(String user) {
        return roleDao.query(user, pagination.getPageSize(20), pagination.getPageNum());
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
    public boolean hasType(String user, String editor, Type type) {
        if (validator.isEmpty(user))
            user = userHelper.id();
        if (validator.isEmpty(user))
            return false;

        RoleModel role = find(user, editor);

        return role != null && role.getType() <= type.ordinal();
    }

    @Override
    public void save(String user, String editor, Type type) {
        RoleModel role = find(user, editor);
        if (role == null) {
            role = new RoleModel();
            role.setUser(user);
            role.setEditor(editor);
        }
        role.setType(type.ordinal());
        save(role);
    }

    @Override
    public void modify(String editor, Timestamp time) {
        roleDao.query(editor).getList().forEach(role -> {
            role.setModify(time);
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
        else
            roleDao.query(editor).getList().forEach(this::delete);
    }

    private void delete(RoleModel role) {
        roleDao.delete(role);
        cache.remove(getCacheKey(role.getUser(), role.getEditor()));
    }

    private String getCacheKey(String user, String editor) {
        return CACHE_USER_EDITOR + user + "," + editor;
    }
}
