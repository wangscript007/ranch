package org.lpw.ranch.dbtool.column;

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
@Repository(ColumnModel.NAME + ".dao")
class ColumnDaoImpl implements ColumnDao {
    @Inject
    private Validator validator;
    @Inject
    private DataSource dataSource;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<ColumnModel> query(String table, String name) {
        StringBuilder where = new StringBuilder("c_table=?");
        List<Object> args = new ArrayList<>();
        args.add(table);
        if (!validator.isEmpty(name))
            where.append(" and c_name like ").append(dataSource.getDialect(null).getLike(name, true, true));

        return liteOrm.query(new LiteQuery(ColumnModel.class).where(where.toString()).order("c_sort"), args.toArray());
    }

    @Override
    public ColumnModel findById(String id) {
        return liteOrm.findById(ColumnModel.class, id);
    }

    @Override
    public int count(String table) {
        return liteOrm.count(new LiteQuery(ColumnModel.class).where("c_table=?"), new Object[]{table});
    }

    @Override
    public void save(ColumnModel column) {
        liteOrm.save(column);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(ColumnModel.class, id);
    }
}
