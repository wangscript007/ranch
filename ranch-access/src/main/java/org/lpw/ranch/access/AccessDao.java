package org.lpw.ranch.access;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface AccessDao {
    PageList<AccessModel> query(String host, String uri, String user, String userAgent, Timestamp start, Timestamp end,
                                int pageSize, int pageNum);

    void save(AccessModel access);

    void delete(Timestamp time);
}
