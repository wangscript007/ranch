package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface WeixinService {
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
     * 获取微信配置集。
     *
     * @return 信配置集。
     */
    JSONArray query();

    /**
     * 保存微信配置。
     * 如果key存在则更新；key不存在则新增。
     *
     * @param weixin 微信配置。
     */
    void save(WeixinModel weixin);
}
