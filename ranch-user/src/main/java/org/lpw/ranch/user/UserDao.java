package org.lpw.ranch.user;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface UserDao {
    PageList<UserModel> query(String idcard, String name, String nick, String mobile, String email, String code,
                              int minGrade, int maxGrade, int state, Timestamp registerStart, Timestamp registerEnd,
                              int pageSize, int pageNum);

    UserModel findById(String id);

    UserModel findByCode(String code);

    void save(UserModel user);
}
