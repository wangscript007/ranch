package org.lpw.ranch.log;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

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
    }
}
