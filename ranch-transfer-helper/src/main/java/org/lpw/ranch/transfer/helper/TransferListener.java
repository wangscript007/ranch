package org.lpw.ranch.transfer.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * 转账监听器。
 *
 * @author lpw
 */
public interface TransferListener {
    /**
     * 获取转账类型。
     *
     * @return 付款类型。
     */
    String getType();

    /**
     * 重置转账订单状态。
     *
     * @param object 订单信息。
     */
    void resetState(JSONObject object);
}
