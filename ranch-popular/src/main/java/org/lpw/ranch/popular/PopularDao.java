package org.lpw.ranch.popular;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface PopularDao {
    PageList<PopularModel> query(String key, int pageSize, int pageNum);

    PopularModel find(String key, String value);

    void save(PopularModel popular);

    void delete(String id);
}
