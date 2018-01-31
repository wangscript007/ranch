package org.lpw.ranch.device.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface DeviceHelper {
    /**
     * 检索设备绑定信息集。
     *
     * @param user     用户，为空则不限制。
     * @param appCode  APP编码，为空则不限制。
     * @param type     类型：android、ios，为空则不限制。
     * @param macId    Mac ID，为空则不限制。
     * @param version  版本号，为空则不限制。
     * @param pageSize 每页显示记录数。
     * @param pageNum  显示页码值。
     * @return 设备绑定信息集。
     */
    JSONObject query(String user, String appCode, String type, String macId, String version, int pageSize, int pageNum);

    /**
     * 获取设备信息。
     *
     * @param appCode APP编码。
     * @param macId   Mac ID。
     * @return 设备信息。
     */
    JSONObject find(String appCode, String macId);
}
