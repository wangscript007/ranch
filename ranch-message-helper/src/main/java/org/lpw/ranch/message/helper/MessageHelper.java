package org.lpw.ranch.message.helper;

/**
 * @author lpw
 */
public interface MessageHelper {
    /**
     * 接收者类型。
     */
    enum Type {
        /**
         * 好友。
         */
        Friend,
        /**
         * 群组。
         */
        Group
    }

    /**
     * 消息格式
     */
    enum Format {
        /**
         * 文本。
         */
        Text,
        /**
         * 音频。
         */
        Audio,
        /**
         * 视频。
         */
        Video,
        /**
         * 文件。
         */
        File,
        /**
         * 红包。
         */
        RedBag,
        /**
         * 转账。
         */
        Transfer,
        /**
         * 名片。
         */
        Card,
        /**
         * 公告。
         */
        Notice,
        /**
         * 通知。
         */
        Notify
    }

    /**
     * 发送消息。
     *
     * @param type     接收者类型。
     * @param receiver 接收者ID。
     * @param format   消息格式。
     * @param content  内容。
     * @param deadline 失效时长，单位：秒。
     * @return 消息ID。
     */
    String send(Type type, String receiver, Format format, String content, int deadline);

    /**
     * 发送通知。
     *
     * @param type     接收者类型。
     * @param receiver 接收者ID。
     * @param content  内容。
     * @param deadline 失效时长，单位：秒。
     * @return 消息ID。
     */
    String notice(Type type, String receiver, String content, int deadline);
}
