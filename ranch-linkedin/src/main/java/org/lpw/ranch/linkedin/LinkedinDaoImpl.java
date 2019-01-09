package org.lpw.ranch.linkedin;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(LinkedinModel.NAME + ".dao")
class LinkedinDaoImpl implements LinkedinDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<LinkedinModel> query() {
        return liteOrm.query(new LiteQuery(LinkedinModel.class), null);
    }

    @Override
    public LinkedinModel findById(String id) {
        return liteOrm.findById(LinkedinModel.class, id);
    }

    @Override
    public LinkedinModel findByKey(String key) {
        return liteOrm.findOne(new LiteQuery(LinkedinModel.class).where("c_key=?"), new Object[]{key});
    }

    @Override
    public void save(LinkedinModel linkedin) {
        liteOrm.save(linkedin);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(LinkedinModel.class, id);
    }
}
