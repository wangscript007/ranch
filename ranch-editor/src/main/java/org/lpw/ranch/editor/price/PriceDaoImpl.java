package org.lpw.ranch.editor.price;

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
@Repository(PriceModel.NAME + ".dao")
class PriceDaoImpl implements PriceDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<PriceModel> query(String type, String group, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_group", DaoOperation.Equals, group);

        return liteOrm.query(new LiteQuery(PriceModel.class).where(where.toString()).order("c_type,c_group")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PriceModel findById(String id) {
        return liteOrm.findById(PriceModel.class, id);
    }

    @Override
    public PriceModel find(String type, String group) {
        return liteOrm.findOne(new LiteQuery(PriceModel.class).where("c_type=? and c_group=?"), new Object[]{type, group});
    }

    @Override
    public void save(PriceModel price) {
        liteOrm.save(price);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(PriceModel.class, id);
    }
}
