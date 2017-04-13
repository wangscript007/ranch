package org.lpw.ranch.account.log;

import org.lpw.ranch.account.AccountModel;

/**
 * @author lpw
 */
public interface LogService {
    /**
     * 状态。
     */
    enum State {
        /**
         * 待处理。
         */
        New,
        /**
         * 审核通过。
         */
        Pass,
        /**
         * 审核不通过。
         */
        Refuse,
        /**
         * 已完成。
         */
        Complete
    }

    /**
     * 新增日志。
     *
     * @param account 账户信息。
     * @param type    类型。
     * @param amount  数量。
     * @param state   状态。
     */
    void create(AccountModel account, String type, int amount, State state);
}