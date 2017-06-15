package org.lpw.ranch.account.helper;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface AccountHelper {
    /**
     * 存入。
     *
     * @param user   用户，为空则使用当前用户。
     * @param owner  所有者。
     * @param type   类型。
     * @param amount 数量。
     * @param map    参数集。
     * @return 账户信息；如果存入失败则返回空JSON。
     */
    JSONObject deposit(String user, String owner, int type, int amount, Map<String, String> map);

    /**
     * 设置审核通过。
     *
     * @param logId 日志ID。
     * @return 如果设置成功则返回true；否则返回false。
     */
    boolean pass(String logId);
}
