package org.lpw.ranch.dbtool.schema;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
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
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<SchemaModel> query(String group, String key, String type, String ip, String name) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_group", DaoOperation.Equals, group);
        daoHelper.like(null, where, args, "c_key", key);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.like(null, where, args, "c_ip", ip);
        daoHelper.like(null, where, args, "c_name", name);

        return liteOrm.query(new LiteQuery(SchemaModel.class).where(where.toString()).order("c_sort"), args.toArray());
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
