package org.lpw.ranch.dbtool.column;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface ColumnDao {
    PageList<ColumnModel> query(String table, String name);

    ColumnModel findById(String id);

    int count(String table);

    void save(ColumnModel column);

    void delete(String id);
}
