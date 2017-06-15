package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface WeixinService {
    /**
     * 微信key是否存在验证器Bean名称。
     * 默认错误信息key=WeixinModel.NAME+.not-exists。
     */
    String VALIDATOR_EXISTS = WeixinModel.NAME + ".validator.exists";
    /**
     * 微信APP ID是否不存在验证器Bean名称。
     * 默认错误信息key=WeixinModel.NAME+.exists。
     */
    String VALIDATOR_NOT_EXISTS = WeixinModel.NAME + ".validator.not-exists";

    /**
     * 获取微信配置集。
     *
     * @return 信配置集。
     */
    JSONArray query();

    /**
     * 根据引用key获取微信配置。
     *
     * @param key 引用key。
     * @return 微信配置；如果不存在则返回null。
     */
    WeixinModel findByKey(String key);

    /**
     * 根据APP ID获取微信配置。
     *
     * @param appId APP ID。
     * @return 微信配置；如果不存在则返回null。
     */
    WeixinModel findByAppId(String appId);

    /**
     * 保存微信配置。
     * 如果key存在则更新；key不存在则新增。
     *
     * @param weixin 微信配置。
     */
    void save(WeixinModel weixin);

    /**
     * 验证echo信息是否正确。
     *
     * @param appId     微信公众号AppID。
     * @param signature 签名。
     * @param timestamp 时间戳。
     * @param nonce     随机数。
     * @param echostr   echo字符串。
     * @return 如果验证成功则返回echostr原值；否则返回"failure"。
     */
    String echo(String appId, String signature, String timestamp, String nonce, String echostr);

    /**
     * 认证用户信息。
     *
     * @param key  配置key。
     * @param code 微信认证code。
     * @return 如果认证通过则返回用户授权信息，否则返回空JSON。
     */
    JSONObject auth(String key, String code);
}
