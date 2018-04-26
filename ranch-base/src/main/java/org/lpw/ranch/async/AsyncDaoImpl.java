package org.lpw.ranch.async;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * @author lpw
 */
@Repository(AsyncModel.NAME + ".dao")
class AsyncDaoImpl implements AsyncDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<AsyncModel> query(int state, Timestamp timeout) {
        return liteOrm.query(new LiteQuery(AsyncModel.class).where("c_state=? and c_timeout<=?"),
                new Object[]{state, timeout});
    }

    @Override
    public AsyncModel findById(String id) {
        return liteOrm.findById(AsyncModel.class, id);
    }

    @Override
    public void save(AsyncModel async) {
        liteOrm.save(async);
    }
}
