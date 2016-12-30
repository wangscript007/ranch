package org.lpw.ranch.user;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
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
}
