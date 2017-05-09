package org.lpw.ranch.user.auth;

import org.lpw.ranch.audit.AuditModel;
import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface AuthDao {
    PageList<AuditModel> query(String user);

    AuthModel findByUid(String uid);

    void save(AuthModel auth);

    void delete(AuthModel auth);
}
