package org.lpw.ranch.user.auth;

/**
 * @author lpw
 */
interface AuthDao {
    AuthModel findByUid(String uid);

    void save(AuthModel auth);
}
