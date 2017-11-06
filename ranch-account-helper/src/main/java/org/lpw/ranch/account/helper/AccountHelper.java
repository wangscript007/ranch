package org.lpw.ranch.account.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface AccountHelper {
    /**
     * 检索用户账户集。
     *
     * @param user  用户，为空则使用当前用户。
     * @param owner 所有者；为null表示所有。
     * @param fill  是否填充关联数据。
     * @return 账户集。
     */
    JSONArray queryUser(String user, String owner, boolean fill);

    /**
     * 检索账户信息并合并。
     *
     * @param user  用户，为空则使用当前用户。
     * @param owner 所有者；为null表示所有。
     * @param fill  是否填充关联数据。
     * @return 账户信息。
     */
    JSONObject merge(String user, String owner, boolean fill);

    /**
     * 存入。
     *
     * @param user    用户，为空则使用当前用户。
     * @param owner   所有者。
     * @param type    类型。
     * @param channel 渠道。
     * @param amount  数量。
     * @param pass    自动设置审核通过。
     * @param map     参数集。
     * @return 账户信息；如果存入失败则返回空JSON。
     */
    JSONObject deposit(String user, String owner, int type, String channel, int amount, boolean pass, Map<String, String> map);

    /**
     * 消费。
     *
     * @param user    用户，为空则使用当前用户。
     * @param owner   所有者。
     * @param type    类型。
     * @param channel 渠道。
     * @param amount  数量。
     * @param pass    自动设置审核通过。
     * @param map     参数集。
     * @return 账户信息；如果消费失败则返回空JSON。
     */
    JSONObject consume(String user, String owner, int type, String channel, int amount, boolean pass, Map<String, String> map);

    /**
     * 盈利。
     *
     * @param user    用户，为空则使用当前用户。
     * @param owner   所有者。
     * @param type    类型。
     * @param channel 渠道。
     * @param amount  数量。
     * @param pass    自动设置审核通过。
     * @param map     参数集。
     * @return 账户信息；如果盈利失败则返回空JSON。
     */
    JSONObject profit(String user, String owner, int type, String channel, int amount, boolean pass, Map<String, String> map);

    /**
     * 退款。
     *
     * @param user    用户，为空则使用当前用户。
     * @param owner   所有者。
     * @param type    类型。
     * @param channel 渠道。
     * @param amount  数量。
     * @param pass    自动设置审核通过。
     * @param map     参数集。
     * @return 账户信息；如果退款失败则返回空JSON。
     */
    JSONObject refund(String user, String owner, int type, String channel, int amount, boolean pass, Map<String, String> map);


    /**
     * 设置审核通过。
     *
     * @param logIds 日志ID集，多个ID间以逗号分隔。
     * @return 设置成功的ID值集。
     */
    JSONArray pass(String logIds);


    /**
     * 设置审核不通过。
     *
     * @param logIds 日志ID集，多个ID间以逗号分隔。
     * @return 设置成功的ID值集。
     */
    JSONArray reject(String logIds);
}
