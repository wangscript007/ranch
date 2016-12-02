package org.lpw.ranch.classify;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface ClassifyDao {
    PageList<ClassifyModel> query(int pageSize, int pageNum);

    PageList<ClassifyModel> query(String code, int pageSize, int pageNum);

    ClassifyModel findById(String id);

    void save(ClassifyModel classify);
}
