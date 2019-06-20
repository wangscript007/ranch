package org.lpw.ranch.weixin.qrcode;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(QrcodeModel.NAME + ".dao")
class QrcodeDaoImpl implements QrcodeDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<QrcodeModel> query(String key, String appId, String user, String name, String scene, Timestamp[] time,
                                       int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_key", DaoOperation.Equals, key);
        daoHelper.where(where, args, "c_app_id", DaoOperation.Equals, appId);
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_time", DaoOperation.GreaterEquals, time[0]);
        daoHelper.where(where, args, "c_time", DaoOperation.LessEquals, time[1]);
        daoHelper.like(null, where, args, "c_name", name);
        daoHelper.like(null, where, args, "c_scene", scene);

        return liteOrm.query(new LiteQuery(QrcodeModel.class).where(where.toString()).order("c_time desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public QrcodeModel find(String key, String user, String name) {
        return liteOrm.findOne(new LiteQuery(QrcodeModel.class).where("c_user=? and c_name=? and c_key=?").order("c_time desc"),
                new Object[]{user, name, key});
    }

    @Override
    public void save(QrcodeModel qrcode) {
        liteOrm.save(qrcode);
    }
}
