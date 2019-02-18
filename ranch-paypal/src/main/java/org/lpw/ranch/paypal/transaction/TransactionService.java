package org.lpw.ranch.paypal.transaction;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface TransactionService {
    /**
     * 检索。
     *
     * @param user    用户ID。
     * @param creates 时间范围，格式：yyyy-MM-dd或yyyy-MM-dd HH:mm:ss。
     * @return 收据集。
     */
    JSONObject query(String user, String[] creates);

    /**
     * 验证。
     *
     * @param key     引用key。
     * @param tradeNo 交易ID。
     * @return 验证结果。
     */
    String verify(String key, String tradeNo);
}
