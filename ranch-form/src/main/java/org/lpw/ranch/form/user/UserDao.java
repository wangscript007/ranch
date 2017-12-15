package org.lpw.ranch.form.user;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface UserDao {
    PageList<UserModel> query(String user, Timestamp start, Timestamp end, int pageSize, int pageNum);

    UserModel find(String user, String form);

    void save(UserModel user);

    void delete(String id);
}
