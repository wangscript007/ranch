package org.lpw.ranch.user.auth;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author lpw
 */
@Repository(AuthModel.NAME + ".dao")
class AuthDaoImpl implements AuthDao {
    @Autowired
    protected LiteOrm liteOrm;

    @Override
    public AuthModel findByUid(String uid) {
        return liteOrm.findOne(new LiteQuery(AuthModel.class).where("c_uid=?"), new Object[]{uid});
    }
}
