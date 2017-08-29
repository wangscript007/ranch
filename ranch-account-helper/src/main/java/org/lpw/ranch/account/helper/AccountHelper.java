package org.lpw.ranch.account.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface AccountHelper {
    /**
     * 检索账户集。
     *
     * @param user  用户，为空则使用当前用户。
     * @param owner 所有者；为null表示所有。
     * @param fill  是否填充关联数据。
     * @return 账户集。
     */
    JSONArray query(String user, String owner, boolean fill);

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
     * @param map     参数集。
     * @return 账户信息；如果存入失败则返回空JSON。
     */
    JSONObject deposit(String user, String owner, int type, String channel, int amount, Map<String, String> map);

    /**
     * 消费。
     *
     * @param user    用户，为空则使用当前用户。
     * @param owner   所有者。
     * @param type    类型。
     * @param channel 渠道。
     * @param amount  数量。
     * @param map     参数集。
     * @return 账户信息；如果消费失败则返回空JSON。
     */
    JSONObject consume(String user, String owner, int type, String channel, int amount, Map<String, String> map);


    /**
     * 设置审核通过。
     *
     * @param logIds 日志ID集，多个ID间以逗号分隔。
     * @return 如果设置成功则返回true；否则返回false。
     */
    boolean pass(String logIds);


    /**
     * 设置审核不通过。
     *
     * @param logIds 日志ID集，多个ID间以逗号分隔。
     * @return 如果设置成功则返回true；否则返回false。
     */
    boolean reject(String logIds);
}
