package org.lpw.ranch.paypal;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface PaypalDao {
    PageList<PaypalModel> query();

    PaypalModel findByKey(String key);

    PaypalModel findByAppId(String appId);

    void save(PaypalModel paypal);

    void delete(String id);
}
