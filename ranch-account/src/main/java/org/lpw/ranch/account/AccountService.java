package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface AccountService {
    /**
     * 存入。
     *
     * @param user   用户。
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 数量。
     * @return 账户信息；如果存入失败则返回null。
     */
    JSONObject deposit(String user, String owner, int type, int amount);

    /**
     * 取出。
     *
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 数量。
     * @return 账户信息；如果取出失败则返回null。
     */
    JSONObject withdraw(String owner, int type, int amount);

    /**
     * 奖励。
     *
     * @param user   用户。
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 数量。
     * @return 账户信息；如果奖励失败则返回null。
     */
    JSONObject reward(String user, String owner, int type, int amount);

    /**
     * 盈利。
     *
     * @param user   用户。
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 数量。
     * @return 账户信息；如果盈利失败则返回null。
     */
    JSONObject profit(String user, String owner, int type, int amount);

    /**
     * 消费。
     *
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 数量。
     * @return 账户信息；如果消费失败则返回null。
     */
    JSONObject consume(String owner, int type, int amount);
}
