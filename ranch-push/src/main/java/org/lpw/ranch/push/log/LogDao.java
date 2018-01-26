package org.lpw.ranch.push.log;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface LogDao {
//    PageList<LogModel> query(String user, String receiver, String appCode, String sender, int state, Timestamp start, Timestamp end);

    int count(String receiver, int state);

    void save(LogModel log);

    void setState(String id, int state);
}
