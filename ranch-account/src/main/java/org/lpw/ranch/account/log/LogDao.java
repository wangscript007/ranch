package org.lpw.ranch.account.log;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface LogDao {
    PageList<LogModel> query(String user, String type, int state, Timestamp start, Timestamp end, int pageSize, int pageNum);

    LogModel findById(String id);

    void save(LogModel log);
}
