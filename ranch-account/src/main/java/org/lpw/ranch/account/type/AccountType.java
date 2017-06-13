package org.lpw.ranch.account.type;

import org.lpw.ranch.account.AccountModel;
import org.lpw.ranch.account.log.LogModel;

import java.util.Map;

/**
 * @author lpw
 */
public interface AccountType {
    /**
     * 获取类型名称。
     *
     * @return 类型名称。
     */
    String getName();

    /**
     * 账户变动。
     *
     * @param account 账户。
     * @param amount  数量。
     * @param map     参数集。
     * @return 日志ID值。
     */
    String change(AccountModel account, int amount, Map<String, String> map);

    /**
     * 结算。
     *
     * @param account 账户。
     * @param log     操作日志。
     * @return 如果结算成功则返回true；否则返回false。
     */
    boolean complete(AccountModel account, LogModel log);
}
