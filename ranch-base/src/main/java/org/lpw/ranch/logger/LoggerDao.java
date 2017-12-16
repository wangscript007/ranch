package org.lpw.ranch.logger;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface LoggerDao {
    PageList<LoggerModel> query(String key, Timestamp start, Timestamp end, int pageSize, int pageNum);

    void save(LoggerModel logger);
}
