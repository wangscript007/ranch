package org.lpw.ranch.weixin.info;

/**
 * @author lpw
 */
interface InfoDao {
    InfoModel findByOpenId(String openId);

    void save(InfoModel info);
}
