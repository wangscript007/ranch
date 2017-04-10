package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface AccountService {
    /**
     * 存入。
     *
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 金额。
     * @return 账户信息。
     */
    JSONObject deposit(String owner, int type, int amount);

    /**
     * 取出。
     *
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 金额。
     * @return 账户信息。
     */
    JSONObject withdraw(String owner, int type, int amount);
}
