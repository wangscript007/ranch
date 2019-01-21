package org.lpw.ranch.log;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface LogDao {
    void save(LogModel log);

    void delete(Timestamp time);
}
