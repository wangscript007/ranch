package org.lpw.ranch.device;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(DeviceModel.NAME + ".dao")
class DeviceDaoImpl implements DeviceDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public DeviceModel find(String appCode, String macId) {
        return liteOrm.findOne(new LiteQuery(DeviceModel.class).where("c_app_code=? and c_mac_id=?"), new Object[]{appCode, macId});
    }

    @Override
    public void save(DeviceModel device) {
        liteOrm.save(device);
    }
}
