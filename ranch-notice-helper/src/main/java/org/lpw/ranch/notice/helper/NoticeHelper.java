package org.lpw.ranch.notice.helper;

/**
 * @author lpw
 */
public interface NoticeHelper {
    /**
     * 发送通知。
     *
     * @param user    用户。
     * @param type    类型。
     * @param subject 标题。
     * @param content 内容。
     * @param link    链接。
     */
    void send(String user, String type, String subject, String content, String link);

    /**
     * 发送通知。
     *
     * @param users   用户集。
     * @param type    类型。
     * @param subject 标题。
     * @param content 内容。
     * @param link    链接。
     */
    void send(String[] users, String type, String subject, String content, String link);
}
