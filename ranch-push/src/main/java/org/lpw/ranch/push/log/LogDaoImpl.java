package org.lpw.ranch.push.log;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
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
    public int count(String receiver, int state) {
        return liteOrm.count(new LiteQuery(LogModel.class).where("c_receiver=? and c_state=?"), new Object[]{receiver, state});
    }

    @Override
    public void save(LogModel log) {
        liteOrm.save(log);
    }

    @Override
    public void setState(String id, int state) {
        liteOrm.update(new LiteQuery(LogModel.class).set("c_state=?").where("c_id=?"), new Object[]{state, id});
    }
}
