package org.lpw.ranch.weixin.info;

/**
 * @author lpw
 */
public interface InfoService {
    /**
     * 保存详情信息。
     *
     * @param key     引用KEY。
     * @param appId   APP ID。
     * @param unionId Union ID。
     * @param openId  Open ID。
     */
    void save(String key, String appId, String unionId, String openId);
}
