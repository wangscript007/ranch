package org.lpw.ranch.user;

/**
 * @author lpw
 */
interface UserDao {
    UserModel findById(String id);

    int count(String code);

    void save(UserModel user);
}
