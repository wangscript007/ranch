package org.lpw.ranch.classify;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface ClassifyDao {
    PageList<ClassifyModel> query(int pageSize, int pageNum);

    PageList<ClassifyModel> query(String code, String key, String value, String name, int pageSize, int pageNum);

    PageList<ClassifyModel> query(String code);

    ClassifyModel findById(String id);

    ClassifyModel findByCodeKey(String code, String key);

    void save(ClassifyModel classify);
}
