package org.lpw.ranch.user.auth;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface AuthDao {
    PageList<AuthModel> query(String user);

    AuthModel findByUid(String uid);

    void save(AuthModel auth);

    void delete(AuthModel auth);
}
