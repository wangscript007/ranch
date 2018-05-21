package org.lpw.ranch.weixin.info;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(InfoModel.NAME + ".dao")
class InfoDaoImpl implements InfoDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public InfoModel findByOpenId(String openId) {
        return liteOrm.findOne(new LiteQuery(InfoModel.class).where("c_open_id=?"), new Object[]{openId});
    }

    @Override
    public void save(InfoModel info) {
        liteOrm.save(info);
    }
}
