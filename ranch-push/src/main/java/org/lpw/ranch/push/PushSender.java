package org.lpw.ranch.push;

/**
 * 推送器。
 *
 * @author lpw
 */
public interface PushSender {
    /**
     * 获取名称。
     *
     * @return 名称。
     */
    String getName();

    /**
     * 推送。
     *
     * @param receiver 接收者。
     * @param subject  标题。
     * @param content  内容。
     * @return 推送结果：true-成功；false-失败。
     */
    boolean send(String receiver, String subject, String content);
}
