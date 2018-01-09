package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONObject;

/**
 * 推送器。
 *
 * @author lpw
 */
public interface PushSender {
    /**
     * 获取名称。
     *
     * @return 名称。
     */
    String getName();

    /**
     * 推送。
     *
     * @param push     推送配置。
     * @param receiver 接收者。
     * @param args     参数集。
     * @return 推送结果：true-成功；false-失败。
     */
    boolean send(PushModel push, String receiver, JSONObject args);
}
