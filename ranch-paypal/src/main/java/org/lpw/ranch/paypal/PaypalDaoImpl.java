package org.lpw.ranch.paypal;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(PaypalModel.NAME + ".dao")
class PaypalDaoImpl implements PaypalDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<PaypalModel> query() {
        return liteOrm.query(new LiteQuery(PaypalModel.class), null);
    }

    @Override
    public PaypalModel findByKey(String key) {
        return liteOrm.findOne(new LiteQuery(PaypalModel.class).where("c_key=?"), new Object[]{key});
    }

    @Override
    public PaypalModel findByAppId(String appId) {
        return liteOrm.findOne(new LiteQuery(PaypalModel.class).where("c_app_id=?"), new Object[]{appId});
    }

    @Override
    public void save(PaypalModel paypal) {
        liteOrm.save(paypal);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(PaypalModel.class, id);
    }
}
