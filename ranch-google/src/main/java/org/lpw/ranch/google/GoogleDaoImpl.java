package org.lpw.ranch.google;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(GoogleModel.NAME + ".dao")
class GoogleDaoImpl implements GoogleDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<GoogleModel> query() {
        return liteOrm.query(new LiteQuery(GoogleModel.class), null);
    }

    @Override
    public GoogleModel findByKey(String key) {
        return liteOrm.findOne(new LiteQuery(GoogleModel.class).where("c_key=?"), new Object[]{key});
    }

    @Override
    public void save(GoogleModel google) {
        liteOrm.save(google);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(GoogleModel.class, id);
    }
}
