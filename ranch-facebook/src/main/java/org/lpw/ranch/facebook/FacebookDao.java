package org.lpw.ranch.facebook;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface FacebookDao {
    PageList<FacebookModel> query();

    FacebookModel findById(String id);

    FacebookModel findByKey(String key);

    void save(FacebookModel facebook);

    void delete(String id);
}
