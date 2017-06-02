package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface WeixinService {
    /**
     * 获取微信配置集。
     *
     * @return 信配置集。
     */
    JSONArray query();

    /**
     * 保存微信配置。
     * 如果key存在则更新；key不存在则新增。
     *
     * @param weixin 微信配置。
     */
    void save(WeixinModel weixin);
}
