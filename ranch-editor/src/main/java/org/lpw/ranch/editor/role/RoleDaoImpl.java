package org.lpw.ranch.editor.role;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lpw
 */
@Repository(RoleModel.NAME + ".dao")
class RoleDaoImpl implements RoleDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<RoleModel> query(String user, int template, String etype, Set<Integer> states, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_template", DaoOperation.Equals, template);
        daoHelper.where(where, args, "c_etype", DaoOperation.Equals, etype);
        daoHelper.in(where, args, "c_state", states);

        return liteOrm.query(new LiteQuery(RoleModel.class).where(where.toString()).order("c_modify desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<RoleModel> query(String editor, boolean sortable) {
        return liteOrm.query(new LiteQuery(RoleModel.class).where("c_editor=?")
                .order(sortable ? "c_type,c_create" : ""), new Object[]{editor});
    }

    @Override
    public PageList<RoleModel> query(Set<String> users) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.in(where, args, "c_user", users);

        return liteOrm.query(new LiteQuery(RoleModel.class).where(where.toString()), args.toArray());
    }

    @Override
    public RoleModel findById(String id) {
        return liteOrm.findById(RoleModel.class, id);
    }

    @Override
    public RoleModel find(String user, String editor) {
        return liteOrm.findOne(new LiteQuery(RoleModel.class).where("c_editor=? and c_user=?"), new Object[]{editor, user});
    }

    @Override
    public void save(RoleModel role) {
        liteOrm.save(role);
    }

    @Override
    public void delete(RoleModel role) {
        liteOrm.delete(role);
    }
}
