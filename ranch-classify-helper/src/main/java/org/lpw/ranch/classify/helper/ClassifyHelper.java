package org.lpw.ranch.classify.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 分类服务支持。
 *
 * @author lpw
 */
public interface ClassifyHelper {
    /**
     * 获取分类信息。
     *
     * @param id ID值。
     * @return JSON数据，如果未找到则返回仅包含id属性的JSON数据。
     */
    JSONObject get(String id);

    /**
     * 获取分类信息集。
     *
     * @param code 编码前缀，会自动匹配【code+%】。
     * @param key  包含的关键词。
     * @param name 包含的名称。
     * @return JSON数据集，如果未找到则返回仅包含id属性的JSON数据。
     */
    JSONArray list(String code, String key, String name);

    /**
     * 填充分类信息。
     *
     * @param array 要填充的数据集。
     * @param names 要填充的属性名称集。
     * @return 填充后的数据集。
     */
    JSONArray fill(JSONArray array, String[] names);
}
