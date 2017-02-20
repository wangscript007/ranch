package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.recycle.RecycleService;

import java.util.Map;

/**
 * @author lpw
 */
public interface ClassifyService extends RecycleService {
    /**
     * 分类信息是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = ClassifyModel.NAME + ".validator.exists";

    /**
     * 检索分类信息集。
     *
     * @param code 编码前缀。
     * @return 分类信息集。
     */
    JSONObject query(String code);

    /**
     * 检索分类信息树。
     *
     * @param code 编码前缀。
     * @return 分类信息树。
     */
    JSONArray tree(String code);

    /**
     * 查找分类信息。
     *
     * @param ids ID集。
     * @return 分类信息，如果不存在则返回空JSON。
     */
    JSONObject get(String[] ids);

    /**
     * 检索指定关键词的分类信息集。
     *
     * @param code 编码前缀。
     * @param key  包含的关键词。
     * @param name 包含的名称。
     * @return 分类信息集。
     */
    JSONArray list(String code, String key, String name);

    /**
     * 创建新分类。
     *
     * @param map 参数集。
     * @return 分类JSON格式数据。
     */
    JSONObject create(Map<String, String> map);

    /**
     * 修改分类信息。
     *
     * @param id   ID值。
     * @param code 编码。
     * @param key  关键词。
     * @param name 名称。
     * @param map  参数集。
     * @return 分类JSON格式数据。
     */
    JSONObject modify(String id, String code, String key, String name, Map<String, String> map);

    /**
     * 刷新缓存。
     */
    void refresh();

    /**
     * 检索分类信息。
     *
     * @param id ID值。
     * @return 分类信息；如果不存在则返回null。
     */
    ClassifyModel findById(String id);
}
