package org.lpw.ranch.weixin.reply;

import org.lpw.ranch.weixin.WeixinModel;

/**
 * 回复修改器。
 *
 * @author lpw
 */
public interface ReplyAlter {
    /**
     * 修改回复。
     *
     * @param weixin         微信配置。
     * @param openId         用户Open ID。
     * @param receiveType    接收类型。
     * @param receiveMessage 接收消息。
     * @param eventKey       事件key。
     * @param reply          回复。
     */
    void alter(WeixinModel weixin, String openId, String receiveType, String receiveMessage, String eventKey, ReplyModel reply);
}
