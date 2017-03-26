package org.lpw.ranch.last.helper;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface LastHelper {
    /**
     * 获取最新日志信息。
     *
     * @param type 类型。
     * @return 日志信息，如果不存在则返回空JSON。
     */
    JSONObject find(String type);

    /**
     * 保存最新日志信息。
     *
     * @param type 类型。
     * @param map  扩展参数集。
     * @return 日志信息。
     */
    JSONObject save(String type, Map<String, String> map);
}
