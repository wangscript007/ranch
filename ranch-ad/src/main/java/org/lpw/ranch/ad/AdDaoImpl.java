package org.lpw.ranch.ad;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(AdModel.NAME + ".dao")
class AdDaoImpl implements AdDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<AdModel> query(String type, int state, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_state", DaoOperation.Equals, state);

        return liteOrm.query(new LiteQuery(AdModel.class).where(where.toString()).order("c_state desc,c_type,c_sort")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<AdModel> query(String type, int state) {
        return liteOrm.query(new LiteQuery(AdModel.class).where("c_type=? and c_state=?").order("c_sort"), new Object[]{type, state});
    }

    @Override
    public AdModel findById(String id) {
        return liteOrm.findById(AdModel.class, id);
    }

    @Override
    public void save(AdModel ad) {
        liteOrm.save(ad);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(AdModel.class, id);
    }
}
