package org.lpw.ranch.appstore;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface AppstoreDao {
    PageList<AppstoreModel> query(int pageSize, int pageNum);

    AppstoreModel findByProductId(String productId);

    void save(AppstoreModel appstore);

    void delete(String id);
}
