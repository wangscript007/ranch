package org.lpw.ranch.user;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
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
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<UserModel> query(String idcard, String name, String nick, String mobile, String email, String code,
                                     int minGrade, int maxGrade, int state, Timestamp registerStart, Timestamp registerEnd,
                                     int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_code", DaoOperation.Equals, code);
        daoHelper.where(where, args, "c_mobile", DaoOperation.Equals, mobile);
        daoHelper.where(where, args, "c_idcard", DaoOperation.Equals, idcard);
        daoHelper.like(null, where, args, "c_name", name);
        daoHelper.like(null, where, args, "c_nick", nick);
        daoHelper.like(null, where, args, "c_email", email);
        daoHelper.where(where, args, "c_grade", DaoOperation.GreaterEquals, minGrade);
        daoHelper.where(where, args, "c_grade", DaoOperation.LessEquals, maxGrade);
        daoHelper.where(where, args, "c_state", DaoOperation.Equals, state);
        daoHelper.where(where, args, "c_register", DaoOperation.GreaterEquals, registerStart);
        daoHelper.where(where, args, "c_register", DaoOperation.LessEquals, registerEnd);

        return liteOrm.query(new LiteQuery(UserModel.class).where(where.toString()).order("c_register desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public UserModel findById(String id) {
        return liteOrm.findById(UserModel.class, id);
    }

    @Override
    public UserModel findByCode(String code) {
        return liteOrm.findOne(new LiteQuery(UserModel.class).where("c_code=?"), new Object[]{code});
    }

    @Override
    public void save(UserModel user) {
        liteOrm.save(user);
    }
}
