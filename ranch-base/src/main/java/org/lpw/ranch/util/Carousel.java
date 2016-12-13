package org.lpw.ranch.util;

import net.sf.json.JSONObject;

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
     * 获取用户信息。
     *
     * @param id ID值。
     * @return JSON数据，如果未找到则返回仅包含id属性的JSON数据。
     */
    JSONObject getUser(String id);
}
