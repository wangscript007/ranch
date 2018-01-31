package org.lpw.ranch.device;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface DeviceDao {
    PageList<DeviceModel> query(String user, String appCode, String type, String macId, String version, int pageSize, int pageNum);

    DeviceModel find(String appCode, String macId);

    void save(DeviceModel device);
}
