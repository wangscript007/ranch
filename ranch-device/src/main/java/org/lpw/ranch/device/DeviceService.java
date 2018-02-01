package org.lpw.ranch.device;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface DeviceService {
    /**
     * 检索设备绑定信息集。
     *
     * @param user    用户，为空则不限制。
     * @param appCode APP编码，为空则不限制。
     * @param type    类型：android、ios，为空则不限制。
     * @param macId   Mac ID，为空则不限制。
     * @param version 版本号，为空则不限制。
     * @return 设备绑定信息集。
     */
    JSONObject query(String user, String appCode, String type, String macId, String version);

    /**
     * 获取设备绑定信息。
     *
     * @param appCode APP编码。
     * @param macId   Mac ID。
     * @return 设备绑定信息。
     */
    JSONObject find(String appCode, String macId);

    /**
     * 保存设备绑定信息。
     *
     * @param user    用户，为空则为当前用户。
     * @param appCode APP编码。
     * @param type    类型：android、ios。
     * @param macId   Mac ID。
     * @param version 版本号。
     * @return 设备绑定信息。
     */
    JSONObject save(String user, String appCode, String type, String macId, String version);

    /**
     * 解绑。
     *
     * @param user    用户，为空则为当前用户。
     * @param appCode APP编码。
     * @param macId   Mac ID。
     */
    void unbind(String user, String appCode, String macId);
}
