package org.lpw.ranch.device;

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
@Repository(DeviceModel.NAME + ".dao")
class DeviceDaoImpl implements DeviceDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<DeviceModel> query(String user, String appCode, String type, String macId, String version, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_app_code", DaoOperation.Equals, appCode);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_mac_id", DaoOperation.Equals, macId);
        daoHelper.where(where, args, "c_version", DaoOperation.Equals, version);

        return liteOrm.query(new LiteQuery(DeviceModel.class).where(where.toString()).order("c_time desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public DeviceModel find(String appCode, String macId) {
        return liteOrm.findOne(new LiteQuery(DeviceModel.class).where("c_app_code=? and c_mac_id=?"), new Object[]{appCode, macId});
    }

    @Override
    public void save(DeviceModel device) {
        liteOrm.save(device);
    }

    @Override
    public void delete(DeviceModel device) {
        liteOrm.delete(device);
    }
}
