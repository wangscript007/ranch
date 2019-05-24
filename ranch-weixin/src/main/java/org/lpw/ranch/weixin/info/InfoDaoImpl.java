package org.lpw.ranch.weixin.info;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * @author lpw
 */
@Repository(InfoModel.NAME + ".dao")
class InfoDaoImpl implements InfoDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<InfoModel> query(Timestamp time) {
        return liteOrm.query(new LiteQuery(InfoModel.class).where("c_time>=?"), new Object[]{time});
    }

    @Override
    public InfoModel find(String openId) {
        return liteOrm.findOne(new LiteQuery(InfoModel.class).where("c_open_id=?"), new Object[]{openId});
    }

    @Override
    public InfoModel find(String appId, String unionId) {
        return liteOrm.findOne(new LiteQuery(InfoModel.class).where("c_union_id=? and c_app_id=?"), new Object[]{unionId, appId});
    }

    @Override
    public void save(InfoModel info) {
        liteOrm.save(info);
    }
}
