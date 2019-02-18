package org.lpw.ranch.paypal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface PaypalService {
    /**
     * Paypal APP ID是否不存在验证器Bean名称。
     * 默认错误信息key=PaypalModel.NAME+.not-exists。
     */
    String VALIDATOR_EXISTS = PaypalModel.NAME + ".validator.exists";

    /**
     * Paypal APP ID是否不存在验证器Bean名称。
     * 默认错误信息key=PaypalModel.NAME+.exists。
     */
    String VALIDATOR_NOT_EXISTS = PaypalModel.NAME + ".validator.not-exists";

    /**
     * 获取Paypal配置集。
     *
     * @return 信配置集。
     */
    JSONArray query();

    /**
     * 根据KEY获取Paypal配置。
     *
     * @param key 引用KEY。
     * @return Paypal配置；如果不存在则返回null。
     */
    PaypalModel findByKey(String key);

    /**
     * 根据APP ID获取Paypal配置。
     *
     * @param appId APP ID。
     * @return Paypal配置；如果不存在则返回null。
     */
    PaypalModel findByAppId(String appId);

    /**
     * 保存Paypal配置。
     * 如果key存在则更新；key不存在则新增。
     *
     * @param Paypal Paypal配置。
     * @return 配置值。
     */
    JSONObject save(PaypalModel Paypal);

    /**
     * 删除Paypal配置。
     *
     * @param id ID值。
     */
    void delete(String id);
}
