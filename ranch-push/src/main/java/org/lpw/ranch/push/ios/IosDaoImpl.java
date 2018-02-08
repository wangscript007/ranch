package org.lpw.ranch.push.ios;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(IosModel.NAME + ".dao")
class IosDaoImpl implements IosDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<IosModel> query(int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(IosModel.class).order("c_time desc").size(pageSize).page(pageNum), null);
    }

    @Override
    public IosModel findById(String id) {
        return liteOrm.findById(IosModel.class, id);
    }

    @Override
    public IosModel find(String appCode, int destination) {
        return liteOrm.findOne(new LiteQuery(IosModel.class).where("c_app_code=? and c_destination=?"), new Object[]{appCode, destination});
    }

    @Override
    public void save(IosModel ios) {
        liteOrm.save(ios);
    }

    @Override
    public void delete(IosModel ios) {
        liteOrm.delete(ios);
    }
}
