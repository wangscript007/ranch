package org.lpw.ranch.device.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface DeviceHelper {
    /**
     * 获取设备信息。
     *
     * @param appCode APP编码。
     * @param macId   Mac ID。
     * @return 设备信息。
     */
    JSONObject find(String appCode, String macId);
}
