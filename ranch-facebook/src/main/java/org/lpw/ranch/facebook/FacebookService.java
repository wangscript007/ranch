package org.lpw.ranch.facebook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface FacebookService {
    String VALIDATOR_EXISTS_KEY = FacebookModel.NAME + ".validator.exists";

    /**
     * 检索。
     *
     * @return 数据集。
     */
    JSONArray query();

    /**
     * 查找。
     *
     * @param key 引用key。
     * @return 信息，不存在则返回null。
     */
    FacebookModel findByKey(String key);

    /**
     * 保存。
     *
     * @param facebook 数据。
     */
    void save(FacebookModel facebook);

    /**
     * 删除。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 认证。
     *
     * @param key  引用key。
     * @param code 认真code。
     * @return 如果认证通过则返回用户授权信息，否则返回空JSON。
     */
    JSONObject auth(String key, String code);
}
