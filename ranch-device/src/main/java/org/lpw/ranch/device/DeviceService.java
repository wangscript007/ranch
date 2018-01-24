package org.lpw.ranch.device;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface DeviceService {
    /**
     * 获取设备信息。
     *
     * @param appCode APP编码。
     * @param macId   Mac ID。
     * @return 设备信息。
     */
    JSONObject find(String appCode, String macId);

    /**
     * 保存设备信息。
     *
     * @param user    用户，为空则为当前用户。
     * @param appCode APP编码。
     * @param type    类型：android、ios。
     * @param macId   Mac ID。
     * @param version 版本号。
     * @return 设备信息。
     */
    JSONObject save(String user, String appCode, String type, String macId, String version);
}
