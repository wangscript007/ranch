package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.account.log.LogModel;

import java.util.Map;

/**
 * @author lpw
 */
public interface AccountService {
    /**
     * 消费验证器Bean名称。
     * 验证失败key=ranch.account.consume.failure。
     */
    String VALIDATOR_CONSUME = AccountModel.NAME + ".validator.consume";

    /**
     * 检索账户信息集。
     *
     * @param user  用户ID，为空则为当前用户。
     * @param owner 所有者ID，null则表示所有。
     * @return 账户信息集。
     */
    JSONArray query(String user, String owner);

    /**
     * 存入。
     *
     * @param user   用户。
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 数量。
     * @param map    参数集。
     * @return 账户信息；如果存入失败则返回null。
     */
    JSONObject deposit(String user, String owner, int type, int amount, Map<String, String> map);

    /**
     * 取出。
     *
     * @param user   用户。
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 数量。
     * @param map    参数集。
     * @return 账户信息；如果取出失败则返回null。
     */
    JSONObject withdraw(String user, String owner, int type, int amount, Map<String, String> map);

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
     * @param user   用户。
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 数量。
     * @return 账户信息；如果消费失败则返回null。
     */
    JSONObject consume(String user, String owner, int type, int amount);

    /**
     * 结算。
     *
     * @param log 操作日志。
     * @return 结算成功则返回true；否则返回false。
     */
    boolean complete(LogModel log);
}
