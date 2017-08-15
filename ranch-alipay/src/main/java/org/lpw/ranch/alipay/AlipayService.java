package org.lpw.ranch.alipay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface AlipayService {
    /**
     * 支付宝APP ID是否不存在验证器Bean名称。
     * 默认错误信息key=AlipayModel.NAME+.not-exists。
     */
    String VALIDATOR_EXISTS = AlipayModel.NAME + ".validator.exists";

    /**
     * 支付宝APP ID是否不存在验证器Bean名称。
     * 默认错误信息key=AlipayModel.NAME+.exists。
     */
    String VALIDATOR_NOT_EXISTS = AlipayModel.NAME + ".validator.not-exists";

    /**
     * 获取支付宝配置集。
     *
     * @return 信配置集。
     */
    JSONArray query();

    /**
     * 根据KEY获取支付宝配置。
     *
     * @param key 引用KEY。
     * @return 支付宝配置；如果不存在则返回null。
     */
    AlipayModel findByKey(String key);

    /**
     * 根据APP ID获取支付宝配置。
     *
     * @param appId APP ID。
     * @return 支付宝配置；如果不存在则返回null。
     */
    AlipayModel findByAppId(String appId);

    /**
     * 保存支付宝配置。
     * 如果key存在则更新；key不存在则新增。
     *
     * @param alipay 支付宝配置。
     * @return 配置值。
     */
    JSONObject save(AlipayModel alipay);

    /**
     * 删除支付宝配置。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 发起手机WEB端支付。
     *
     * @param key       引用key。
     * @param user      用户ID。
     * @param subject   订单名称。
     * @param amount    支付金额，单位：分。
     * @param notifyUrl 异步通知URL地址。
     * @param returnUrl 同步结果URL地址。
     * @return 支付内容；如果发起失败则返回null。
     */
    String quickWapPay(String key, String user, String subject, int amount, String notifyUrl, String returnUrl);

    /**
     * 发起PC WEB端支付。
     *
     * @param key       引用key。
     * @param user      用户ID。
     * @param subject   订单名称。
     * @param amount    支付金额，单位：分。
     * @param notifyUrl 异步通知URL地址。
     * @param returnUrl 同步结果URL地址。
     * @return 支付内容；如果发起失败则返回null。
     */
    String fastInstantTradePay(String key, String user, String subject, int amount, String notifyUrl, String returnUrl);

    /**
     * 发起APP端支付。
     *
     * @param key       引用key。
     * @param user      用户ID。
     * @param subject   订单名称。
     * @param amount    支付金额，单位：分。
     * @param notifyUrl 异步通知URL地址。
     * @return 支付内容；如果发起失败则返回null。
     */
    String quickMsecurityPay(String key, String user, String subject, int amount, String notifyUrl);

    /**
     * 异步通知。
     *
     * @param appId   APP ID。
     * @param orderNo 订单号。
     * @param tradeNo 网关订单号。
     * @param amount  金额。
     * @param status  状态。
     * @param map     参数集。
     * @return 执行成功则返回true；否则返回false。
     */
    boolean notify(String appId, String orderNo, String tradeNo, String amount, String status, Map<String, String> map);
}
