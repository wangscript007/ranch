package org.lpw.ranch.push.log;

/**
 * @author lpw
 */
interface LogDao {
    int count(String receiver, int state);

    void save(LogModel log);

    void setState(String id, int state);
}
