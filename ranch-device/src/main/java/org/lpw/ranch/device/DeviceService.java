package org.lpw.ranch.device;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface DeviceService {
    /**
     * 保存设备信息。
     *
     * @param user    用户，为空则为当前用户。
     * @param appCode App编码。
     * @param type    类型：android、ios。
     * @param macId   Mac ID。
     * @param version 版本号。
     * @return 设备信息。
     */
    JSONObject save(String user, String appCode, String type, String macId, String version);
}
