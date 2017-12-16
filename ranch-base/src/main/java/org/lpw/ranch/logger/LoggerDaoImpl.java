package org.lpw.ranch.logger;

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
@Repository(LoggerModel.NAME + ".dao")
class LoggerDaoImpl implements LoggerDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<LoggerModel> query(String key, Timestamp start, Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder("c_key=?");
        List<Object> args = new ArrayList<>();
        args.add(key);
        if (start != null) {
            where.append(" and c_time>=?");
            args.add(start);
        }
        if (end != null) {
            where.append(" and c_time<=?");
            args.add(end);
        }

        return liteOrm.query(new LiteQuery(LoggerModel.class).where(where.toString()).order("c_time").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public void save(LoggerModel logger) {
        liteOrm.save(logger);
    }
}
