package org.lpw.ranch.classify;

import org.lpw.tephra.dao.orm.PageList;

import java.util.Set;

/**
 * @author lpw
 */
interface ClassifyDao {
    PageList<ClassifyModel> query(int pageSize, int pageNum);

    PageList<ClassifyModel> query(String code, String key, String name, int pageSize, int pageNum);

    ClassifyModel findById(String id);

    ClassifyModel findByCodeValue(String code, String value);

    void save(ClassifyModel classify);

    void delete(String code);

    void restore(Set<String> codes);
}
