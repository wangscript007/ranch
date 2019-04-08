package org.lpw.ranch.google;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface GoogleService {
    /**
     * key是否存在验证器Bean名称。
     */
    String VALIDATOR_KEY_EXISTS = GoogleModel.NAME + ".key.exists";

    /**
     * 检索。
     *
     * @return 配置集。
     */
    JSONArray query();

    /**
     * 查找key。
     *
     * @param key key。
     * @return 配置，如果不存在则返回null。
     */
    GoogleModel findByKey(String key);

    /**
     * 保存。
     * 如果key存在则更新；key不存在则新增。
     *
     * @param google 配置。
     */
    void save(GoogleModel google);

    /**
     * 删除。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 登入校验。
     *
     * @param key   饮用key。
     * @param token Token。
     * @return 校验结果。
     */
    JSONObject auth(String key, String token);
}
