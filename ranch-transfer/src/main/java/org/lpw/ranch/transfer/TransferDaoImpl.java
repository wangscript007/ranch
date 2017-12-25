package org.lpw.ranch.transfer;

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
@Repository(TransferModel.NAME + ".dao")
class TransferDaoImpl implements TransferDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<TransferModel> query(String type, String appId, String user, String orderNo, String billNo, String tradeNo, int state, Timestamp start, Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        append(where, args, "c_start", ">=", start);
        append(where, args, "c_start", "<=", end);
        append(where, args, "c_type", "=", type);
        append(where, args, "c_app_id", "=", appId);
        append(where, args, "c_user", "=", user);
        append(where, args, "c_order_no", "=", orderNo);
        append(where, args, "c_bill_no", "=", billNo);
        append(where, args, "c_trade_no", "=", tradeNo);
        if (state > -1)
            append(where, args, "c_state", "=", state);

        return liteOrm.query(new LiteQuery(TransferModel.class).where(where.toString()).order("c_start desc").size(pageSize).page(pageNum), args.toArray());
    }

    private void append(StringBuilder where, List<Object> args, String name, String operation, Object value) {
        if (validator.isEmpty(value))
            return;

        if (args.size() > 0)
            where.append(" and ");
        where.append(name).append(operation).append("?");
        args.add(value);
    }

    @Override
    public TransferModel findById(String id) {
        return liteOrm.findById(TransferModel.class, id);
    }

    @Override
    public TransferModel findByOrderNo(String orderNo) {
        return liteOrm.findOne(new LiteQuery(TransferModel.class).where("c_order_no=?"), new Object[]{orderNo});
    }

    @Override
    public void save(TransferModel transfer) {
        liteOrm.save(transfer);
        liteOrm.close();
    }
}
