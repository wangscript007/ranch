package org.lpw.ranch.account.log;

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
@Repository(LogModel.NAME + ".dao")
class LogDaoImpl implements LogDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<LogModel> query(String user, String owner, String type, String channel, int state, Timestamp start,
                                    Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_owner", DaoOperation.Equals, owner);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_channel", DaoOperation.Equals, channel);
        daoHelper.where(where, args, "c_state", DaoOperation.Equals, state);
        daoHelper.where(where, args, "c_start", DaoOperation.GreaterEquals, start);
        daoHelper.where(where, args, "c_start", DaoOperation.LessEquals, end);

        return liteOrm.query(new LiteQuery(LogModel.class).where(where.toString()).order("c_start desc,c_index desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<LogModel> query(int restate) {
        return liteOrm.query(new LiteQuery(LogModel.class).where("c_restate=?"), new Object[]{restate});
    }

    @Override
    public LogModel findById(String id) {
        return liteOrm.findById(LogModel.class, id);
    }

    @Override
    public void save(LogModel log) {
        liteOrm.save(log);
        liteOrm.close();
    }
}
