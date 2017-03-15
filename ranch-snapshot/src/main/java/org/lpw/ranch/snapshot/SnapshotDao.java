package org.lpw.ranch.snapshot;

/**
 * @author lpw
 */
interface SnapshotDao {
    SnapshotModel findById(String id);

    SnapshotModel findByMd5(String md5);

    void save(SnapshotModel snapshot);
}
