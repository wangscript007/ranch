package org.lpw.ranch.push.log;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface LogDao {
    PageList<LogModel> query(String user, String receiver, String appCode, String sender, int state,
                             Timestamp start, Timestamp end, int pageSize, int pageNum);

    PageList<LogModel> query(String user, String appCode, int stateStart, int stateEnd, int pageSize, int pageNum);

    LogModel findNewest(String user, String appCode, int state);

    int count(String user, String appCode, int state);

    void save(LogModel log);

    void setState(String id, int state, int newState);

    void setState(String user, String appCode, int state, int newState);
}
