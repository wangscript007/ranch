package org.lpw.ranch.logger;

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

    @Override
    public PageList<LoggerModel> query(String key, int state, Timestamp start, Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        append(where, args, "c_key", key, "=");
        if (state > -1)
            append(where, args, "c_state", state, "=");
        append(where, args, "c_time", start, ">=");
        append(where, args, "c_time", end, "<=");

        return liteOrm.query(new LiteQuery(LoggerModel.class).where(where.toString()).order("c_time desc").size(pageSize).page(pageNum), args.toArray());
    }

    private void append(StringBuilder where, List<Object> args, String name, Object value, String operation) {
        if (validator.isEmpty(value))
            return;

        if (!args.isEmpty())
            where.append(" and ");
        where.append(name).append(operation).append('?');
        args.add(value);
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
