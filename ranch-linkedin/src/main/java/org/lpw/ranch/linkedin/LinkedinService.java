package org.lpw.ranch.linkedin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface LinkedinService {
    String VALIDATOR_EXISTS_KEY = LinkedinModel.NAME + ".validator.exists";

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
    LinkedinModel findByKey(String key);

    /**
     * 保存。
     *
     * @param linkedin 数据。
     */
    void save(LinkedinModel linkedin);

    /**
     * 删除。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 认证。
     *
     * @param key         引用key。
     * @param code        认真code。
     * @param redirectUri 转发请求地址。
     * @return 如果认证通过则返回用户授权信息，否则返回空JSON。
     */
    JSONObject auth(String key, String code, String redirectUri);
}
