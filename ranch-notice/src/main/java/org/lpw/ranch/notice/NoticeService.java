package org.lpw.ranch.notice;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface NoticeService {
    String ALL_USER = "00000000-user-user-user-000000000000";

    /**
     * 检索全局通知集。
     *
     * @param type 类型，为空则表示全部。
     * @param read 已读：-1-全部；0-否；1-是。
     * @return 通知集。
     */
    JSONObject global(String type, int read);

    /**
     * 检索通知集。
     *
     * @param type 类型，为空则表示全部。
     * @param read 已读：-1-全部；0-否；1-是。
     * @return 通知集。
     */
    JSONObject query(String type, int read);

    /**
     * 检索发送所有人通知集。
     *
     * @param type    类型，为空则表示全部。
     * @param subject 标题，模糊匹配。
     * @param time    时间范围，格式：yyyy-MM-dd或yyyy-MM-dd HH:mm:ss。
     * @return 通知集。
     */
    JSONObject query(String type, String subject, String[] time);

    /**
     * 发送全局通知。
     *
     * @param type    类型。
     * @param subject 标题。
     * @param content 内容。
     * @param link    链接。
     */
    void send(String type, String subject, String content, String link);

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

    /**
     * 标记已读。
     *
     * @param id ID值。
     */
    void read(String id);

    /**
     * 全部标记已读。
     *
     * @param type 类型，为空则表示全部。
     */
    void reads(String type);
}
