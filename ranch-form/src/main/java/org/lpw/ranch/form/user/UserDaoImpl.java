package org.lpw.ranch.form.user;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(UserModel.NAME + ".dao")
class UserDaoImpl implements UserDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<UserModel> query(String user, Timestamp start, Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        where.append("c_user=?");
        args.add(user);
        if (start != null) {
            where.append(" and c_create>=?");
            args.add(start);
        }
        if (end != null) {
            where.append(" and c_create<=?");
            args.add(end);
        }

        return liteOrm.query(new LiteQuery(UserModel.class).where(where.toString())
                .order("c_create desc").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public UserModel find(String user, String form) {
        return liteOrm.findOne(new LiteQuery(UserModel.class).where("c_form=? and c_user=?"), new Object[]{form, user});
    }

    @Override
    public void save(UserModel user) {
        liteOrm.save(user);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(UserModel.class, id);
    }
}
