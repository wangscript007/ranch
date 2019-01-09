package org.lpw.ranch.linkedin;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface LinkedinDao {
    PageList<LinkedinModel> query();

    LinkedinModel findById(String id);

    LinkedinModel findByKey(String key);

    void save(LinkedinModel linkedin);

    void delete(String id);
}
