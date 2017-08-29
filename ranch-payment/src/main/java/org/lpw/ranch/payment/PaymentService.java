package org.lpw.ranch.payment;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface PaymentService {
    /**
     * 订单号是否存在验证器Bean名称。
     * 默认错误信息key=PaymentModel.NAME+.not-exists。
     */
    String VALIDATOR_EXISTS = PaymentModel.NAME + ".validator.exists";

    /**
     * 检索订单信息集。
     *
     * @param type    类型，为空则表示全部。
     * @param user    用户ID，为空则表示全部。
     * @param orderNo 订单号，为空则表示全部。
     * @param tradeNo 网关订单号，为空则表示全部。
     * @param state   状态，-1则表示全部。
     * @param start   开始日期，格式yyyy-MM-dd，为空则表示全部。
     * @param end     结束日期，格式yyyy-MM-dd，为空则表示全部。
     * @return 订单信息集。
     */
    JSONObject query(String type, String user, String orderNo, String tradeNo, int state, String start, String end);

    /**
     * 设置订单信息为成功。
     *
     * @param id  订单ID。
     * @param map 参数集。
     * @return 订单信息。
     */
    JSONObject success(String id, Map<String, String> map);

    /**
     * 发送通知。
     *
     * @param id 订单ID。
     * @return 订单信息。
     */
    JSONObject notice(String id);

    /**
     * 查找支付订单信息。
     *
     * @param uk 订单唯一索引值，可以是ID或订单号。
     * @return 支付订单信息；如果未找到则返回null。
     */
    PaymentModel find(String uk);

    /**
     * 创建支付订单。
     *
     * @param type   类型。
     * @param user   用户ID；如果为空则使用当前用户ID。
     * @param amount 金额，单位：分。
     * @param notice 通知。
     * @param map    参数集。
     * @return 订单信息。
     */
    JSONObject create(String type, String user, int amount, String notice, Map<String, String> map);

    /**
     * 订单完成。
     *
     * @param orderNo 订单号。
     * @param amount  金额，单位：分。
     * @param tradeNo 网关订单号。
     * @param state   状态：1-成功；2-失败。
     * @param map     参数集。
     * @return 订单信息；如果处理失败则返回空JSON。
     */
    JSONObject complete(String orderNo, int amount, String tradeNo, int state, Map<String, String> map);
}
