package org.lpw.ranch.dbtool.table;

import org.lpw.tephra.dao.jdbc.DataSource;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(TableModel.NAME + ".dao")
class TableDaoImpl implements TableDao {
    @Inject
    private Validator validator;
    @Inject
    private DataSource dataSource;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<TableModel> query(String schema, String group, String name) {
        StringBuilder where = new StringBuilder("c_schema=?");
        List<Object> args = new ArrayList<>();
        args.add(schema);
        if (!validator.isEmpty(group)) {
            where.append(" and c_group=?");
            args.add(group);
        }
        if (!validator.isEmpty(name))
            where.append(" and c_name like ").append(dataSource.getDialect(null).getLike(name, true, true));

        return liteOrm.query(new LiteQuery(TableModel.class).where(where.toString()).order("c_sort"), args.toArray());
    }

    @Override
    public TableModel findById(String id) {
        return liteOrm.findById(TableModel.class, id);
    }

    @Override
    public int count(String schema) {
        return liteOrm.count(new LiteQuery(TableModel.class).where("c_schema=?"), new Object[]{schema});
    }

    @Override
    public void save(TableModel table) {
        liteOrm.save(table);
    }

    @Override
    public void delete(TableModel table) {
        liteOrm.delete(table);
    }
}
