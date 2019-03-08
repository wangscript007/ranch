package org.lpw.ranch.popular;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface PopularService {
    /**
     * 检索。
     *
     * @param key 引用key。
     * @return 数据集。
     */
    JSONObject query(String key);

    /**
     * 发布。
     *
     * @param key  引用key。
     * @param size 数据量。
     * @return 数据集。
     */
    JSONArray publish(String key, int size);

    /**
     * 增加。
     *
     * @param key   引用key。
     * @param value 值。
     */
    void increase(String key, String value);

    /**
     * 删除。
     *
     * @param id ID值。
     */
    void delete(String id);
}
