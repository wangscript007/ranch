package org.lpw.ranch.payment;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(PaymentModel.NAME + ".dao")
class PaymentDaoImpl implements PaymentDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<PaymentModel> query(String type, String appId, String user, String orderNo, String billNo, String tradeNo,
                                        int state, Timestamp start, Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_start", DaoOperation.GreaterEquals, start);
        daoHelper.where(where, args, "c_start", DaoOperation.LessEquals, end);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_app_id", DaoOperation.Equals, appId);
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_order_no", DaoOperation.Equals, orderNo);
        daoHelper.where(where, args, "c_bill_no", DaoOperation.Equals, billNo);
        daoHelper.where(where, args, "c_trade_no", DaoOperation.Equals, tradeNo);
        daoHelper.where(where, args, "c_state", DaoOperation.Equals, state);

        return liteOrm.query(new LiteQuery(PaymentModel.class).where(where.toString()).order("c_start desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PaymentModel findById(String id) {
        return liteOrm.findById(PaymentModel.class, id);
    }

    @Override
    public PaymentModel findByOrderNo(String orderNo) {
        return liteOrm.findOne(new LiteQuery(PaymentModel.class).where("c_order_no=?"), new Object[]{orderNo});
    }

    @Override
    public void save(PaymentModel payment) {
        liteOrm.save(payment);
        liteOrm.close();
    }
}
