package org.lpw.ranch.user;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface UserDao {
    UserModel findById(String id);

    int count(String code);

    PageList<UserModel> query(String mobile);

    PageList<UserModel> query(int pageSize, int pageNum);

    void save(UserModel user);
}
