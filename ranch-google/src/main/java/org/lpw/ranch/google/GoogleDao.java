package org.lpw.ranch.google;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface GoogleDao {
    PageList<GoogleModel> query();

    GoogleModel findByKey(String key);

    void save(GoogleModel google);

    void delete(String id);
}
