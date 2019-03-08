package org.lpw.ranch.popular;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(PopularModel.NAME + ".dao")
class PopularDaoImpl implements PopularDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<PopularModel> query(String key, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(PopularModel.class).where("c_key=?").order("c_count desc")
                .size(pageSize).page(pageNum), new Object[]{key});
    }

    @Override
    public PopularModel find(String key, String value) {
        return liteOrm.findOne(new LiteQuery(PopularModel.class).where("c_key=? and c_value=?"), new Object[]{key, value});
    }

    @Override
    public void save(PopularModel popular) {
        liteOrm.save(popular);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(PopularModel.class, id);
    }
}
