package org.lpw.ranch.payment.helper;

import java.util.Map;

/**
 * @author lpw
 */
public interface PaymentHelper {
    /**
     * 创建新支付订单。
     *
     * @param type   支付类型。
     * @param appId  支付APP ID。
     * @param user   用户ID。
     * @param amount 支付金额，单位：分。
     * @param billNo 单据号。
     * @param notice 通知。
     * @param map    参数集。
     * @return 订单号；创建失败则返回null。
     */
    String create(String type, String appId, String user, int amount, String billNo, String notice, Map<String, String> map);

    /**
     * 支付完成。
     *
     * @param orderNo 订单号。
     * @param amount  金额，单位：分。
     * @param tradeNo 网关订单号。
     * @param state   状态：1-成功；2-失败。
     * @param map     参数集。
     * @return 订单号；失败则返回null。
     */
    String complete(String orderNo, int amount, String tradeNo, int state, Map<String, String> map);
}
