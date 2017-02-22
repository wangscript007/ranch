package org.lpw.ranch.user;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(UserModel.NAME + ".dao")
class UserDaoImpl implements UserDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public UserModel findById(String id) {
        return liteOrm.findById(UserModel.class, id);
    }

    @Override
    public UserModel findByCode(String code) {
        return liteOrm.findOne(new LiteQuery(UserModel.class).where("c_code=?"), new Object[]{code});
    }

    @Override
    public PageList<UserModel> query(String mobile) {
        return liteOrm.query(new LiteQuery(UserModel.class).where("c_mobile=?").order("c_register desc"), new Object[]{mobile});
    }

    @Override
    public PageList<UserModel> query(int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(UserModel.class).order("c_register desc").size(pageSize).page(pageNum), null);
    }

    @Override
    public void save(UserModel user) {
        liteOrm.save(user);
    }
}
