package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.OutputStream;
import java.util.Map;

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
     * @return 配置信息。
     */
    JSONObject save(WeixinModel weixin);

    /**
     * 删除微信配置。
     *
     * @param id ID值。
     */
    void delete(String id);

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

    /**
     * 生成支付二维码。
     *
     * @param key          引用key。
     * @param user         用户ID。
     * @param subject      订单名称。
     * @param amount       支付金额，单位：分。
     * @param billNo       单据号。
     * @param notice       异步通知。
     * @param size         二维码图片大小，小于等于0则使用默认值。
     * @param logo         LOGO图片名。
     * @param outputStream 输出流。
     */
    void prepayQrCode(String key, String user, String subject, int amount, String billNo, String notice, int size, String logo, OutputStream outputStream);

    /**
     * 生成支付二维码；并输出Base64数据。
     *
     * @param key     引用key。
     * @param user    用户ID。
     * @param subject 订单名称。
     * @param amount  支付金额，单位：分。
     * @param billNo  单据号。
     * @param notice  异步通知。
     * @param size    二维码图片大小，小于等于0则使用默认值。
     * @param logo    LOGO图片名。
     * @return Base64数据，如果生成失败则返回null。
     */
    String prepayQrCodeBase64(String key, String user, String subject, int amount, String billNo, String notice, int size, String logo);

    /**
     * 生成APP支付参数。
     *
     * @param key     引用key。
     * @param user    用户ID。
     * @param subject 订单名称。
     * @param amount  支付金额，单位：分。
     * @param billNo  单据号。
     * @param notice  异步通知。
     * @return 支付参数。
     */
    JSONObject prepayApp(String key, String user, String subject, int amount, String billNo, String notice);

    /**
     * 异步通知。
     *
     * @param appId      APP ID。
     * @param orderNo    订单号。
     * @param tradeNo    网关订单号。
     * @param amount     金额。
     * @param returnCode 状态码。
     * @param resultCode 业务结果。
     * @param map        参数集。
     * @return 执行成功则返回true；否则返回false。
     */
    boolean notice(String appId, String orderNo, String tradeNo, String amount, String returnCode, String resultCode, Map<String, String> map);
}
