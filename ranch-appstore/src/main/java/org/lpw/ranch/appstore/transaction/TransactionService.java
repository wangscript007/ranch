package org.lpw.ranch.appstore.transaction;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface TransactionService {
    /**
     * 检索。
     *
     * @param user          用户。
     * @param transactionId 交易ID。
     * @param productId     产品ID。
     * @param times         时间范围，格式：yyyy-MM-dd或yyyy-MM-dd HH:mm:ss。
     * @return 数据集。
     */
    JSONObject query(String user, String transactionId, String productId, String[] times);

    /**
     * 保存。
     *
     * @param receipt 收据ID。
     * @param object  收据数据。
     */
    void save(String receipt, JSONObject object);
}
