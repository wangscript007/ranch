package org.lpw.ranch.appstore.receipt;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface ReceiptService {
    /**
     * 检索收据集。
     *
     * @param productId 产品ID。
     * @param times     时间范围，格式：yyyy-MM-dd或yyyy-MM-dd HH:mm:ss。
     * @return 收据集。
     */
    JSONObject query(String productId, String[] times);

    /**
     * 验证。
     *
     * @param data 数据。
     * @return 验证结果。
     */
    JSONObject verify(String data);
}
