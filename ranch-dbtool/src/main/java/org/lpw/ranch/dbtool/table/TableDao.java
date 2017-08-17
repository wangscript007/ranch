package org.lpw.ranch.dbtool.table;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface TableDao {
    PageList<TableModel> query(String schema, String group, String name);

    TableModel findById(String id);

    int count(String schema);

    void save(TableModel table);

    void delete(TableModel table);
}
