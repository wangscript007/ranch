package org.lpw.ranch.dbtool.schema;

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
@Repository(SchemaModel.NAME + ".dao")
class SchemaDaoImpl implements SchemaDao {
    @Inject
    private Validator validator;
    @Inject
    private DataSource dataSource;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<SchemaModel> query(String group, String key, String type, String ip, String name) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        append(where, args, "group", group, true);
        append(where, args, "key", key, false);
        append(where, args, "type", type, true);
        append(where, args, "ip", ip, false);
        append(where, args, "name", name, false);

        return liteOrm.query(new LiteQuery(SchemaModel.class).where(where.toString()).order("c_sort"), args.toArray());
    }

    private void append(StringBuilder where, List<Object> args, String name, String value, boolean equals) {
        if (validator.isEmpty(value))
            return;

        if (!args.isEmpty())
            where.append(" and ");
        where.append("c_").append(name);
        if (equals) {
            where.append("=?");
            args.add(value);
        } else {
            where.append(" like ?");
            args.add(dataSource.getDialect(null).getLike(value, true, true));
        }
    }

    @Override
    public SchemaModel findById(String id) {
        return liteOrm.findById(SchemaModel.class, id);
    }

    @Override
    public SchemaModel findByKey(String key) {
        return liteOrm.findOne(new LiteQuery(SchemaModel.class).where("c_key=?"), new Object[]{key});
    }

    @Override
    public void save(SchemaModel schema) {
        liteOrm.save(schema);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(SchemaModel.class, id);
    }
}
