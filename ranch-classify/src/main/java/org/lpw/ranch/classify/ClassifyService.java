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
     * 分类code+key是否不存在验证器Bean名称。
     */
    String VALIDATOR_CODE_KEY_NOT_EXISTS = ClassifyModel.NAME + ".validator.code-key.not-exists";

    /**
     * 检索分类信息集。
     *
     * @param code  编码前缀。
     * @param key   包含的键。
     * @param value 包含的值。
     * @param name  包含的名称。
     * @return 分类信息集。
     */
    JSONObject query(String code, String key, String value, String name);

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
     * 查找分类信息。
     *
     * @param code 编码。
     * @param key  key值。
     * @return 分类信息，如果不存在则返回空JSON。
     */
    JSONObject find(String code, String key);

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
     * @param code  编码。
     * @param key   键。
     * @param value 值。
     * @param name  名称。
     * @param map   参数集。
     * @return 分类JSON格式数据。
     */
    JSONObject create(String code, String key, String value, String name, Map<String, String> map);

    /**
     * 修改分类信息。
     *
     * @param id    ID值。
     * @param code  编码。
     * @param key   键。
     * @param value 值。
     * @param name  名称。
     * @param map   参数集。
     * @return 分类JSON格式数据。
     */
    JSONObject modify(String id, String code, String key, String value, String name, Map<String, String> map);

    /**
     * 保存分类。
     * 如果code+key已存在则修改；否则新增。
     *
     * @param code  编码。
     * @param key   键。
     * @param value 值。
     * @param name  名称。
     * @param map   参数集。
     * @return 分类JSON格式数据。
     */
    JSONObject save(String code, String key, String value, String name, Map<String, String> map);

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
