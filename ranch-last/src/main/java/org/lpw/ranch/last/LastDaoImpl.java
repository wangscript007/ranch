package org.lpw.ranch.last;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(LastModel.NAME + ".dao")
class LastDaoImpl implements LastDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public LastModel find(String user, String type) {
        return liteOrm.findOne(new LiteQuery(LastModel.class).where("c_user=? and c_type=?"), new Object[]{user, type});
    }

    @Override
    public void save(LastModel last) {
        liteOrm.save(last);
    }
}
