package org.lpw.ranch.push.log;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.push.PushModel;

/**
 * @author lpw
 */
public interface LogService {
    /**
     * 检索数据集。
     *
     * @param user     用户ID，为空则表示不限制。
     * @param receiver 接收地址，为空则表示不限制。
     * @param appCode  APP编码，为空则表示不限制。
     * @param sender   推送器，为空则表示不限制。
     * @param state    状态：-1-全部；0-新建；1-已推送；2-已阅读；3-推送失败。
     * @param start    开始日期，格式：yyyy-MM-dd；为空则表示不限制。
     * @param end      结束日期，格式：yyyy-MM-dd；为空则表示不限制。
     * @return 数据集。
     */
    JSONObject query(String user, String receiver, String appCode, String sender, int state, String start, String end);

    /**
     * 检索用户数据集。
     *
     * @param user    用户。
     * @param appCode APP编码。
     * @return 数据集。
     */
    JSONObject query(String user, String appCode);

    /**
     * 创建日志。
     *
     * @param user     用户。
     * @param receiver 接收者。
     * @param push     推送配置。
     * @param args     参数集。
     * @return 推送日志。
     */
    LogModel create(String user, String receiver, PushModel push, JSONObject args);

    /**
     * 推送。
     *
     * @param log     日志。
     * @param success 是否推送成功：true-成功；false-失败。
     */
    void send(LogModel log, boolean success);

    /**
     * 获取未读数。
     *
     * @param user    用户。
     * @param appCode APP编码。
     * @return 未读数。
     */
    int unread(String user, String appCode);

    /**
     * 获取最新一条未读日志。
     * 如果无未读日志则返回最新一条已读日志。
     *
     * @param user    用户。
     * @param appCode APP编码。
     * @return 未读日志，如果不存在则返回空JSON。
     */
    JSONObject unreadNewest(String user, String appCode);

    /**
     * 阅读。
     *
     * @param id ID值。
     */
    void read(String id);

    /**
     * 阅读全部。
     *
     * @param user    用户。
     * @param appCode APP编码。
     */
    void reads(String user, String appCode);
}
