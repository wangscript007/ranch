package org.lpw.ranch.appstore;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(AppstoreModel.NAME + ".dao")
class AppstoreDaoImpl implements AppstoreDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<AppstoreModel> query(int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(AppstoreModel.class).size(pageSize).page(pageNum).order("c_product_id"), null);
    }

    @Override
    public AppstoreModel findByProductId(String productId) {
        return liteOrm.findOne(new LiteQuery(AppstoreModel.class).where("c_product_id=?"), new Object[]{productId});
    }

    @Override
    public void save(AppstoreModel appstore) {
        liteOrm.save(appstore);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(AppstoreModel.class, id);
    }
}
