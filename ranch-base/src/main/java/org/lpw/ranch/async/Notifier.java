package org.lpw.ranch.async;

/**
 * 通知器。
 *
 * @author lpw
 */
public interface Notifier {
    /**
     * 执行成功。
     *
     * @param result 执行结果。
     */
    void success(String result);

    /**
     * 执行失败。
     */
    void failure();
}
