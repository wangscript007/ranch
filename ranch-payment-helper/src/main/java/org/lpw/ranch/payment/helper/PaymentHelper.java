package org.lpw.ranch.payment.helper;

/**
 * @author lpw
 */
public interface PaymentHelper {
    /**
     * 创建新支付订单。
     *
     * @param type   支付类型。
     * @param user   用户ID。
     * @param amount 支付金额，单位：分。
     * @param notify 通知URL地址。
     * @return 订单号；创建失败则返回null。
     */
    String create(String type, String user, int amount, String notify);

    /**
     * 支付完成。
     *
     * @param orderNo 订单号。
     * @param amount  金额，单位：分。
     * @param tradeNo 网关订单号。
     * @param state   状态：1-成功；2-失败。
     * @return 订单号；失败则返回null。
     */
    String complete(String orderNo, int amount, String tradeNo, int state);
}
