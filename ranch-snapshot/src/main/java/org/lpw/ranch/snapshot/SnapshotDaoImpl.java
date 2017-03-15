package org.lpw.ranch.snapshot;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
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
    public SnapshotModel findByMd5(String md5) {
        return liteOrm.findOne(new LiteQuery(SnapshotModel.class).where("c_md5=?"), new Object[]{md5});
    }

    @Override
    public void save(SnapshotModel snapshot) {
        liteOrm.save(snapshot);
    }
}
