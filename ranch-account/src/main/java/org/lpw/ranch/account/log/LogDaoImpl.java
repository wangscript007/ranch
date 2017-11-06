package org.lpw.ranch.account.log;

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

    @Override
    public PageList<LogModel> query(String user, String owner, String type, String channel, int state, Timestamp start, Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        append(where, args, "c_user", user, "=");
        append(where, args, "c_owner", owner, "=");
        append(where, args, "c_type", type, "=");
        append(where, args, "c_channel", channel, "=");
        if (state > -1)
            append(where, args, "c_state", state, "=");
        append(where, args, "c_start", start, ">=");
        append(where, args, "c_start", end, "<=");

        return liteOrm.query(new LiteQuery(LogModel.class).where(where.toString()).order("c_start desc,c_index desc").size(pageSize).page(pageNum), args.toArray());
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
    public LogModel findById(String id) {
        return liteOrm.findById(LogModel.class, id);
    }

    @Override
    public void save(LogModel log) {
        liteOrm.save(log);
        liteOrm.close();
    }
}
