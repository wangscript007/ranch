package org.lpw.ranch.appstore.receipt;

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
@Repository(ReceiptModel.NAME + ".dao")
class ReceiptDaoImpl implements ReceiptDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<ReceiptModel> query(String user, Timestamp[] times, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_time", DaoOperation.GreaterEquals, times[0]);
        daoHelper.where(where, args, "c_time", DaoOperation.LessEquals, times[1]);

        return liteOrm.query(new LiteQuery(ReceiptModel.class).where(where.toString()).order("c_time desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public void save(ReceiptModel receipt) {
        liteOrm.save(receipt);
    }
}
