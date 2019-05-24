package org.lpw.ranch.weixin.info;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface InfoDao {
    PageList<InfoModel> query(Timestamp time);

    InfoModel find(String openId);

    InfoModel find(String appId, String unionId);

    void save(InfoModel info);
}
