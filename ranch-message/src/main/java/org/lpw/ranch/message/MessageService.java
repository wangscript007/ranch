package org.lpw.ranch.message;

/**
 * @author lpw
 */
public interface MessageService {
    /**
     * 发送信息。
     *
     * @param type     接收者类型：0-好友；1-群组。
     * @param receiver 接收者ID。
     * @param format   消息格式：0-文本；1-图片；2-音频；3-视频；4-文件；5-红包。
     * @param content  内容。
     */
    void send(int type, String receiver, int format, String content);
}
