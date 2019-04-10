package org.lpw.ranch.log;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
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
    public void save(LogModel log) {
        liteOrm.save(log);
        liteOrm.close();
    }

    @Override
    public void delete(Timestamp time) {
        liteOrm.delete(new LiteQuery(LogModel.class).where("c_time<?"), new Object[]{time});
    }
}
