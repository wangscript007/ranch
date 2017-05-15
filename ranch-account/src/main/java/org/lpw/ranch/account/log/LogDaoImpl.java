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
    public PageList<LogModel> query(String user, String type, int state, Timestamp start, Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder("c_state=?");
        List<Object> args = new ArrayList<>();
        args.add(state);
        if (!validator.isEmpty(user)) {
            where.append(" and c_user=?");
            args.add(user);
        }
        if (!validator.isEmpty(type)) {
            where.append(" and c_type=?");
            args.add(type);
        }
        if (start != null) {
            where.append(" and c_start>=?");
            args.add(start);
        }
        if (end != null) {
            where.append(" and c_start<=?");
            args.add(end);
        }

        return liteOrm.query(new LiteQuery(LogModel.class).where(where.toString()).order("c_start desc,c_index desc").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public LogModel findById(String id) {
        return liteOrm.findById(LogModel.class, id);
    }

    @Override
    public void save(LogModel log) {
        liteOrm.save(log);
    }
}
