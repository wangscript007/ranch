package org.lpw.ranch.category;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface CategoryService {
    String LEAF_VALIDATOR=CategoryModel.NAME+".validator.leaf";

    JSONArray query(String key);

    void save(CategoryModel category);

    void delete(String id);
}
