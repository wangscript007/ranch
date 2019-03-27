package org.lpw.ranch.stripe;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface StripeService {
    /**
     * key是否存在验证器Bean名称。
     */
    String VALIDATOR_KEY_EXISTS = StripeModel.NAME + ".key.exists";

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
    StripeModel findByKey(String key);

    /**
     * 保存。
     * 如果key存在则更新；key不存在则新增。
     *
     * @param stripe 配置。
     */
    void save(StripeModel stripe);

    /**
     * 删除。
     *
     * @param id ID值。
     */
    void delete(String id);
}
