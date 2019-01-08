package org.lpw.ranch.facebook;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(FacebookModel.NAME + ".dao")
class FacebookDaoImpl implements FacebookDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<FacebookModel> query() {
        return liteOrm.query(new LiteQuery(FacebookModel.class), null);
    }

    @Override
    public FacebookModel findById(String id) {
        return liteOrm.findById(FacebookModel.class, id);
    }

    @Override
    public FacebookModel findByKey(String key) {
        return liteOrm.findOne(new LiteQuery(FacebookModel.class).where("c_key=?"), new Object[]{key});
    }

    @Override
    public void save(FacebookModel facebook) {
        liteOrm.save(facebook);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(FacebookModel.class, id);
    }
}
