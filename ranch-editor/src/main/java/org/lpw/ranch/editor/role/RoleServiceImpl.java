package org.lpw.ranch.editor.role;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;

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
        roleDao.save(role);
        cache.remove(getCacheKey(user, editor));
    }

    @Override
    public void modify(String editor, Timestamp time) {
        roleDao.query(editor).getList().forEach(role -> {
            role.setModify(time);
            roleDao.save(role);
            cache.remove(getCacheKey(role.getUser(), editor));
        });
    }

    private String getCacheKey(String user, String editor) {
        return CACHE_USER_EDITOR + user + "," + editor;
    }
}
