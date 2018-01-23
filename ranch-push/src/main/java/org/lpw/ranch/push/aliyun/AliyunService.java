package org.lpw.ranch.push.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.IAcsClient;

/**
 * @author lpw
 */
public interface AliyunService {
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
    AliyunModel find(String appCode);

    /**
     * 保存配置。
     *
     * @param appCode   APP编码。
     * @param keyId     KEY ID。
     * @param keySecret KEY密钥。
     * @param appKey    APP KEY。
     * @return 配置信息。
     */
    JSONObject save(String appCode, String keyId, String keySecret, String appKey);

    /**
     * 删除配置。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 获取ACS客户端。
     *
     * @param appCode APP编码。
     * @return ACS客户端，不存在则返回null。
     */
    IAcsClient getIAcsClient(String appCode);
}
