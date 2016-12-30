package org.lpw.ranch.user.auth;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(AuthModel.NAME + ".dao")
class AuthDaoImpl implements AuthDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public AuthModel findByUid(String uid) {
        return liteOrm.findOne(new LiteQuery(AuthModel.class).where("c_uid=?"), new Object[]{uid});
    }

    @Override
    public void save(AuthModel auth) {
        liteOrm.save(auth);
    }
}
