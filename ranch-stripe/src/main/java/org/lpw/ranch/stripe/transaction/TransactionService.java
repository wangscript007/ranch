package org.lpw.ranch.stripe.transaction;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface TransactionService {
    /**
     * 交易号不存在验证器Bean名称。
     */
    String VALIDATOR_TRADE_NO_NOT_EXISTS = TransactionModel.NAME + ".validator.trade-no.not-exists";

    /**
     * 检索。
     *
     * @param user    用户ID。
     * @param creates 时间范围，格式：yyyy-MM-dd或yyyy-MM-dd HH:mm:ss。
     * @return 收据集。
     */
    JSONObject query(String user, String[] creates);

    /**
     * 充值。
     *
     * @param key      引用key。
     * @param amount   金额，单位：分。
     * @param currency 币种。
     * @param tradeNo  交易ID。
     * @return 充值结果。
     */
    JSONObject charge(String key, int amount, String currency, String tradeNo);
}
