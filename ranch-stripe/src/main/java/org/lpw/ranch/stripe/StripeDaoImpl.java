package org.lpw.ranch.stripe;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(StripeModel.NAME + ".dao")
class StripeDaoImpl implements StripeDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<StripeModel> query() {
        return liteOrm.query(new LiteQuery(StripeModel.class), null);
    }

    @Override
    public StripeModel findByKey(String key) {
        return liteOrm.findOne(new LiteQuery(StripeModel.class).where("c_key=?"), new Object[]{key});
    }

    @Override
    public void save(StripeModel stripe) {
        liteOrm.save(stripe);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(StripeModel.class, id);
    }
}
