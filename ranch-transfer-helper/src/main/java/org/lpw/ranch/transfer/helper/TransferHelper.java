package org.lpw.ranch.transfer.helper;

import java.util.Map;

/**
 * @author lpw
 */
public interface TransferHelper {
    /**
     * 创建新转账订单。
     *
     * @param type    转账类型。
     * @param appId   转账APP ID。
     * @param user    用户ID。
     * @param account 账户。
     * @param amount  转账金额，单位：分。
     * @param billNo  单据号。
     * @param notice  通知。
     * @param map     参数集。
     * @return 订单号；创建失败则返回null。
     */
    String create(String type, String appId, String user, String account, int amount, String billNo, String notice, Map<String, String> map);

    /**
     * 转账完成。
     *
     * @param orderNo 订单号。
     * @param amount  金额，单位：分。
     * @param tradeNo 网关订单号。
     * @param state   状态：1-成功；2-失败。
     * @param map     参数集。
     * @return 订单号；失败则返回null。
     */
    String complete(String orderNo, int amount, String tradeNo, int state, Map<String, String> map);
}
