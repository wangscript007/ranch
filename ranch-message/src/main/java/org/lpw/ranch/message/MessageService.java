package org.lpw.ranch.message;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface MessageService {
    /**
     * 发送消息。
     *
     * @param type     接收者类型：0-好友；1-群组。
     * @param receiver 接收者ID。
     * @param format   消息格式：0-文本；1-图片；2-音频；3-视频；4-文件；5-红包；6-转账；7-名片；8-公告；9-通知。
     * @param content  消息内容。
     * @param deadline 有效时间，单位：秒；小于等于0则使用默认设置。
     * @param code     校验码。
     * @return 消息ID，如果发送失败则返回null。
     */
    String send(int type, String receiver, int format, String content, int deadline, String code);

    /**
     * 发送通知。
     *
     * @param type     接收者类型：0-好友；1-群组。
     * @param receiver 接收者ID。
     * @param content  消息内容。
     * @param deadline 失效时长，单位：秒；小于等于0则使用默认设置。
     * @param code     校验码。
     * @return 消息ID，如果发送失败则返回null。
     */
    String notify(int type, String receiver, String content, int deadline, String code);

    /**
     * 检索当前用户最新信息集。
     *
     * @param time 最近一次获取时间戳，单位：毫秒。
     * @return 最新信息集。
     */
    JSONObject newest(long time);
}
