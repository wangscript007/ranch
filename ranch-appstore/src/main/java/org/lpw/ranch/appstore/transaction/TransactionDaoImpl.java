package org.lpw.ranch.appstore.transaction;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(TransactionModel.NAME + ".dao")
class TransactionDaoImpl implements TransactionDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<TransactionModel> query(String user, String transactionId, String productId, Timestamp[] times, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_transaction_id", DaoOperation.Equals, transactionId);
        daoHelper.where(where, args, "c_product_id", DaoOperation.Equals, productId);
        daoHelper.where(where, args, "c_time", DaoOperation.GreaterEquals, times[0]);
        daoHelper.where(where, args, "c_time", DaoOperation.LessEquals, times[1]);

        return liteOrm.query(new LiteQuery(TransactionModel.class).where(where.toString())
                .order("c_time desc").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public TransactionModel findByTransactionId(String transactionId) {
        return liteOrm.findOne(new LiteQuery(TransactionModel.class).where("c_transaction_id=?"), new Object[]{transactionId});
    }

    @Override
    public void save(TransactionModel transaction) {
        liteOrm.save(transaction);
    }
}
