package org.lpw.ranch.user;

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
    public int count(String code) {
        return liteOrm.count(new LiteQuery(UserModel.class).where("c_code=?"), new Object[]{code});
    }

    @Override
    public void save(UserModel user) {
        liteOrm.save(user);
    }
}
