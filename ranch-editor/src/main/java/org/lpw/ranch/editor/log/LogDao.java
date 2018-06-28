package org.lpw.ranch.editor.log;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface LogDao {
    void save(LogModel log);

    void delete(Timestamp time);
}
