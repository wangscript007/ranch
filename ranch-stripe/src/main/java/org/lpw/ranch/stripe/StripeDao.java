package org.lpw.ranch.stripe;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface StripeDao {
    PageList<StripeModel> query();

    StripeModel findByKey(String key);

    void save(StripeModel stripe);

    void delete(String id);
}
