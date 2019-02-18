package org.lpw.ranch.paypal.transaction;

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
    public PageList<TransactionModel> query(String user, Timestamp[] creates, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_create", DaoOperation.GreaterEquals, creates[0]);
        daoHelper.where(where, args, "c_create", DaoOperation.LessEquals, creates[1]);

        return liteOrm.query(new LiteQuery(TransactionModel.class).where(where.toString()).order("c_create desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public int count(String tradeNo) {
        return liteOrm.count(new LiteQuery(TransactionModel.class).where("c_trade_no=?"), new Object[]{tradeNo});
    }

    @Override
    public void save(TransactionModel transaction) {
        liteOrm.save(transaction);
    }
}
