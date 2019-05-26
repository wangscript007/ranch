package org.lpw.ranch.weixin.info;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface InfoService {
    /**
     * 检索。
     *
     * @return 数据集。
     */
    JSONArray query();

    /**
     * 查找Open ID。
     *
     * @param appId   APP ID。
     * @param unionId Union ID。
     * @return Open ID。
     */
    String findOpenId(String appId, String unionId);

    /**
     * 保存详情信息。
     *
     * @param key     引用KEY。
     * @param appId   APP ID。
     * @param unionId Union ID。
     * @param openId  Open ID。
     * @return Union ID。
     */
    String save(String key, String appId, String unionId, String openId);
}
