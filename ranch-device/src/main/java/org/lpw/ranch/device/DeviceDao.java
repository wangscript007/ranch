package org.lpw.ranch.device;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface DeviceDao {
    PageList<DeviceModel> query(String user, String appCode, String type, String macId, String version, int pageSize, int pageNum);

    DeviceModel find(String user, String appCode, String type);

    void save(DeviceModel device);

    void delete(String user, String appCode, String macId);
}
