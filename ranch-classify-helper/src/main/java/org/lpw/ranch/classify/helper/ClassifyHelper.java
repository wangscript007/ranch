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
     * 查找分类信息。
     *
     * @param code 编码。
     * @param key  值。
     * @return 分类JSON数据，如果不存在则返回空JSON数据。
     */
    JSONObject find(String code, String key);

    /**
     * 查找分类value值。
     *
     * @param code 编码。
     * @param key  值。
     * @return 分类value值；如果未找到则返回null。
     */
    String value(String code, String key);

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
     * 填充ID对应的分类信息。
     *
     * @param array 要填充的数据集。
     * @param names 要填充的属性名称集。
     * @return 填充后的数据集。
     */
    JSONArray fill(JSONArray array, String[] names);

    /**
     * 填充key对应分类信息。
     *
     * @param array 要填充的数据集。
     * @param code  编码。
     * @param names 要填充的属性名称集。
     * @return 填充后的数据集。
     */
    JSONArray fill(JSONArray array, String code, String[] names);
}
