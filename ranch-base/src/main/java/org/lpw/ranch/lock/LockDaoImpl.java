package org.lpw.ranch.lock;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(LockModel.NAME + ".dao")
class LockDaoImpl implements LockDao {
    @Inject
    private LiteOrm liteOrm;
    @Value("${ranch.lock.data-source.key:}")
    private String dataSource;

    @Override
    public LockModel findById(String id) {
        return liteOrm.findById(dataSource, LockModel.class, id);
    }

    @Override
    public LockModel findByKey(String key) {
        return liteOrm.findOne(new LiteQuery(LockModel.class).dataSource(dataSource).where("c_key=?").order("c_index"), new Object[]{key});
    }

    @Override
    public void save(LockModel lock) {
        liteOrm.save(dataSource, lock);
        liteOrm.close();
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(dataSource, LockModel.class, id);
        liteOrm.close();
    }
}
