package org.lpw.ranch.lock;

/**
 * @author lpw
 */
interface LockDao {
    LockModel findById(String id);

    LockModel findByKey(String key);

    void save(LockModel lock);

    void delete(String id);
}
