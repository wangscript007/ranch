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
     * 获取支付订单状态。
     *
     * @param object 订单信息。
     * @return 状态：0-新建；1-成功；2-失败。
     */
    int getState(JSONObject object);
}
