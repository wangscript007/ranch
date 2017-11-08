package org.lpw.ranch.account.log;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface LogDao {
    PageList<LogModel> query(String user, String owner, String type, String channel, int state, Timestamp start, Timestamp end, int pageSize, int pageNum);

    PageList<LogModel> query(int restate);

    LogModel findById(String id);

    void save(LogModel log);
}
