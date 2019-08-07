package org.lpw.ranch.weixin.reply;

/**
 * 问题。
 *
 * @author lpw
 */
public interface Question {
    /**
     * 问题。
     *
     * @param openId  Open ID。
     * @param message 内容。
     * @param reply   是否自动回复。
     */
    void question(String openId, String message, boolean reply);
}
