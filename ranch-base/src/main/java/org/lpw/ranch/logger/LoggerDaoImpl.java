package org.lpw.ranch.logger;

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
@Repository(LoggerModel.NAME + ".dao")
class LoggerDaoImpl implements LoggerDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<LoggerModel> query(String key, int state, Timestamp start, Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_key", DaoOperation.Equals, key);
        daoHelper.where(where, args, "c_state", DaoOperation.Equals, state);
        daoHelper.where(where, args, "c_time", DaoOperation.GreaterEquals, start);
        daoHelper.where(where, args, "c_time", DaoOperation.LessEquals, end);

        return liteOrm.query(new LiteQuery(LoggerModel.class).where(where.toString()).order("c_time desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public LoggerModel findById(String id) {
        return liteOrm.findById(LoggerModel.class, id);
    }

    @Override
    public void save(LoggerModel logger) {
        liteOrm.save(logger);
    }
}
