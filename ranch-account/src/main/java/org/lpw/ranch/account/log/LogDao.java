package org.lpw.ranch.account.log;

/**
 * @author lpw
 */
interface LogDao {
    LogModel findById(String id);

    void save(LogModel log);
}
