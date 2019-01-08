package org.lpw.ranch.appstore;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface AppstoreService {
    /**
     * 检索配置集。
     *
     * @return 配置集。
     */
    JSONObject query();

    /**
     * 根据产品ID查找配置。
     *
     * @param productId 产品ID。
     * @return 配置，如果不存在则返回null。
     */
    AppstoreModel findByProductId(String productId);

    /**
     * 保存。
     *
     * @param productId 产品ID。
     * @param name      名称。
     * @param amount    价格，单位：分。
     */
    void save(String productId, String name, int amount);

    /**
     * 删除。
     *
     * @param id ID值。
     */
    void delete(String id);
}
