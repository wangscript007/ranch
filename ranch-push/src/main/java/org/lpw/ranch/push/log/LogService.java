package org.lpw.ranch.push.log;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.push.PushModel;

/**
 * @author lpw
 */
public interface LogService {
    /**
     * 创建日志。
     *
     * @param receiver 接收者。
     * @param push     推送配置。
     * @param args     参数集。
     * @return 推送日志。
     */
    LogModel create(String receiver, PushModel push, JSONObject args);

    /**
     * 推送。
     *
     * @param log     日志。
     * @param success 是否推送成功：true-成功；false-失败。
     */
    void send(LogModel log, boolean success);

    /**
     * 获取未读数。
     *
     * @param receiver 接收者。
     * @return 未读数。
     */
    int unread(String receiver);
}
