package org.lpw.ranch.snapshot;

/**
 * @author lpw
 */
interface SnapshotDao {
    SnapshotModel findById(String id);

    void save(SnapshotModel snapshot);
}
