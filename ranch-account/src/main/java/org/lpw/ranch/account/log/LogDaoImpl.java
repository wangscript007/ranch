package org.lpw.ranch.account.log;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * @author lpw
 */
@Repository(LogModel.NAME + ".dao")
class LogDaoImpl implements LogDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<LogModel> query(String user, Timestamp start, Timestamp end, int pageSize, int pageNum) {
        return null;
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
