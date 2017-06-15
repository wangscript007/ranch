package org.lpw.ranch.alipay;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface AlipayDao {
    PageList<AlipayModel> query();

    AlipayModel findByKey(String key);

    AlipayModel findByAppId(String appId);

    void save(AlipayModel alipay);
}
