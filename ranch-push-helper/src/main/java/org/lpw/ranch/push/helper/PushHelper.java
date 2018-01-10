package org.lpw.ranch.push.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface PushHelper {

    /**
     * 推送。
     *
     * @param key      引用键。
     * @param receiver 接收者。
     * @param args     参数集。
     * @return 推送结果：true-成功；false-失败。
     */
    boolean send(String key, String receiver, JSONObject args);
}
