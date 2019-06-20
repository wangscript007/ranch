package org.lpw.ranch.weixin.qrcode;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface QrcodeDao {
    PageList<QrcodeModel> query(String key, String appId, String user, String name, String scene, Timestamp[] time, int pageSize, int pageNum);

    QrcodeModel find(String key, String user, String name);

    void save(QrcodeModel qrcode);
}
