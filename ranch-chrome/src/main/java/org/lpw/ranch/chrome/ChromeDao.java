package org.lpw.ranch.chrome;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface ChromeDao {
    PageList<ChromeModel> query(String key, String name, int pageSize, int pageNum);

    ChromeModel findByKey(String key);

    void save(ChromeModel chrome);

    void delete(String id);
}
