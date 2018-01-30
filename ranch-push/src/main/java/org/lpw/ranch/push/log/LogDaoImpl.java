package org.lpw.ranch.push.log;

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
@Repository(LogModel.NAME + ".dao")
class LogDaoImpl implements LogDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<LogModel> query(String user, String receiver, String appCode, String sender, int state,
                                    Timestamp start, Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_receiver", DaoOperation.Equals, receiver);
        daoHelper.where(where, args, "c_app_code", DaoOperation.Equals, appCode);
        daoHelper.where(where, args, "c_sender", DaoOperation.Equals, sender);
        daoHelper.where(where, args, "c_state", DaoOperation.Equals, state);
        daoHelper.where(where, args, "c_time", DaoOperation.GreaterEquals, start);
        daoHelper.where(where, args, "c_time", DaoOperation.LessEquals, end);

        return liteOrm.query(new LiteQuery(LogModel.class).where(where.toString()).order("c_time desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<LogModel> query(String user, String appCode, int stateStart, int stateEnd, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(LogModel.class).where("c_user=? and c_app_code=? and c_state between ? and ?")
                .order("c_time desc").size(pageSize).page(pageNum), new Object[]{user, appCode, stateStart, stateEnd});
    }

    @Override
    public LogModel findNewest(String user, String appCode, int state) {
        return liteOrm.findOne(new LiteQuery(LogModel.class).where("c_user=? and c_app_code=? and c_state=?").order("c_time desc"),
                new Object[]{user, appCode, state});
    }

    @Override
    public int count(String user, String appCode, int state) {
        return liteOrm.count(new LiteQuery(LogModel.class).where("c_user=? and c_app_code=? and c_state=?"),
                new Object[]{user, appCode, state});
    }

    @Override
    public void save(LogModel log) {
        liteOrm.save(log);
    }

    @Override
    public void setState(String id, int state, int newState) {
        liteOrm.update(new LiteQuery(LogModel.class).set("c_state=?").where("c_id=? and c_state=?"), new Object[]{newState, id, state});
    }

    @Override
    public void setState(String user, String appCode, int state, int newState) {
        liteOrm.update(new LiteQuery(LogModel.class).set("c_state=?").where("c_user=? and c_app_code=? and c_state=?"),
                new Object[]{newState, user, appCode, state});
    }
}
