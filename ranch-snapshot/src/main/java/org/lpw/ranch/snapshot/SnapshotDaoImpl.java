package org.lpw.ranch.snapshot;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(SnapshotModel.NAME + ".dao")
class SnapshotDaoImpl implements SnapshotDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public SnapshotModel findById(String id) {
        return liteOrm.findById(SnapshotModel.class, id);
    }

    @Override
    public void save(SnapshotModel snapshot) {
        liteOrm.save(snapshot);
    }
}
