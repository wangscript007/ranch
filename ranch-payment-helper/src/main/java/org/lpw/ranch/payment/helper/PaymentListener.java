package org.lpw.ranch.payment.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * 支付监听器。
 *
 * @author lpw
 */
public interface PaymentListener {
    /**
     * 获取支付类型。
     *
     * @return 支付类型。
     */
    String getType();

    /**
     * 重置支付订单状态。
     *
     * @param object      订单信息。
     * @param failureAble 是否允许标记失败。
     */
    void resetState(JSONObject object, boolean failureAble);
}
