package org.lpw.ranch.device;

/**
 * @author lpw
 */
interface DeviceDao {
    DeviceModel find(String appCode, String macId);

    void save(DeviceModel device);
}
