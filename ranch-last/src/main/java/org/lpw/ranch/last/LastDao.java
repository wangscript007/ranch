package org.lpw.ranch.last;

/**
 * @author lpw
 */
interface LastDao {
    LastModel find(String user, String type);

    void save(LastModel last);
}
