package org.lpw.ranch.user.auth;

import org.lpw.ranch.audit.AuditModel;
import org.lpw.tephra.dao.orm.PageList;
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
    public PageList<AuditModel> query(String user) {
        return liteOrm.query(new LiteQuery(AuthModel.class).where("c_user=?"), new Object[]{user});
    }

    @Override
    public AuthModel findByUid(String uid) {
        return liteOrm.findOne(new LiteQuery(AuthModel.class).where("c_uid=?"), new Object[]{uid});
    }

    @Override
    public void save(AuthModel auth) {
        liteOrm.save(auth);
    }
}
