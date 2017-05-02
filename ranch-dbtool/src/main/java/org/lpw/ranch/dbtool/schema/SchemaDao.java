package org.lpw.ranch.dbtool.schema;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface SchemaDao {
    PageList<SchemaModel> query(String group, String key, String type, String ip, String name);

    SchemaModel findById(String id);

    SchemaModel findByKey(String key);

    void save(SchemaModel schema);

    void delete(String id);
}
