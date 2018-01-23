package org.lpw.ranch.push.ios;

import com.alibaba.fastjson.JSONObject;
import com.notnoop.apns.ApnsService;

/**
 * @author lpw
 */
public interface IosService {
    /**
     * 检索配置集。
     *
     * @return 配置集。
     */
    JSONObject query();

    /**
     * 查找配置。
     *
     * @param appCode APP编码。
     * @return 配置信息，如果不存在则返回null。
     */
    IosModel find(String appCode);

    /**
     * 保存配置。
     *
     * @param appCode     APP编码。
     * @param p12         证书，BASE64编码。
     * @param password    证书密码。
     * @param destination 目的地：0-开发；1-正式。
     * @return 配置信息。
     */
    JSONObject save(String appCode, String p12, String password, int destination);

    /**
     * 删除配置。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 获取APNS推送服务。
     *
     * @param appCode APP编码。
     * @return APNS推送服务，如果不存在则返回null。
     */
    ApnsService getApnsService(String appCode);
}
