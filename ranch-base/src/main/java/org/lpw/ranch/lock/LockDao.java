package org.lpw.ranch.lock;

/**
 * @author lpw
 */
interface LockDao {
    LockModel findById(String id);

    LockModel findByMd5(String md5);

    void save(LockModel lock);

    void delete(String id);

    void delete(long expire);
}
