package org.lpw.ranch.push.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface PushHelper {
    /**
     * 推送。
     *
     * @param key      引用键。
     * @param user     用户。
     * @param receiver 接收者。
     * @param args     参数集。
     * @return 推送结果：true-成功；false-失败。
     */
    boolean send(String key, String user, String receiver, JSONObject args);

    /**
     * 推送到指定用户。
     *
     * @param user    用户。
     * @param appCode APP编码。
     * @param subject 标题。
     * @param content 内容。
     * @param args    参数集。
     * @return 推送结果：true-成功；false-失败。
     */
    boolean send(String user, String appCode, String subject, String content, JSONObject args);
}
