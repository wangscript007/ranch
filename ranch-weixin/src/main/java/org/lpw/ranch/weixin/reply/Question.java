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
     */
    void question(String openId, String message);
}
