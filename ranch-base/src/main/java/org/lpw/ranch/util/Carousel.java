package org.lpw.ranch.util;

import net.sf.json.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface Carousel {
    /**
     * 根据ID获取Carousel服务数据。
     *
     * @param key 服务key。
     * @param id  ID值。
     * @return JSON数据，如果未找到则返回仅包含id属性的JSON数据。
     */
    JSONObject get(String key, String id);

    /**
     * 获取Carousel服务JSON数据。
     *
     * @param key       服务key。
     * @param parameter 参数集。
     * @return 服务JSON数据，获取失败则返回空JSON数据。
     */
    JSONObject service(String key, Map<String, String> parameter);

    /**
     * 获取Carousel服务JSON数据。
     *
     * @param key       服务key。
     * @param header    头信息集。
     * @param parameter 参数集。
     * @param cacheTime 缓存时长，单位：分钟。
     * @return 服务JSON数据，获取失败则返回空JSON数据。
     */
    JSONObject service(String key, Map<String, String> header, Map<String, String> parameter, int cacheTime);
}
