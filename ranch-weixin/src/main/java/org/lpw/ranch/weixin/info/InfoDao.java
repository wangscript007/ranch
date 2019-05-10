package org.lpw.ranch.weixin.info;

/**
 * @author lpw
 */
interface InfoDao {
    InfoModel find(String openId);

    InfoModel find(String appId, String unionId);

    void save(InfoModel info);
}
