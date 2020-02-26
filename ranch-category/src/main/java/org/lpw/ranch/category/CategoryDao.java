package org.lpw.ranch.category;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface CategoryDao {
    PageList<CategoryModel> query(String key, String parent);

    CategoryModel findById(String id);

    int count(String key, String parent);

    void save(CategoryModel category);

    void delete(String id);
}
