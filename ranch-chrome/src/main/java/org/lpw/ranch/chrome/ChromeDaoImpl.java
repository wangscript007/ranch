package org.lpw.ranch.chrome;

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
@Repository(ChromeModel.NAME + ".dao")
class ChromeDaoImpl implements ChromeDao {
    @Inject
    private Validator validator;
    @Inject
    private DataSource dataSource;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<ChromeModel> query(String key, String name, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        append(where, args, "c_key", key);
        append(where, args, "c_name", name);

        return liteOrm.query(new LiteQuery(ChromeModel.class).where(where.toString()).size(pageSize).page(pageNum), args.toArray());
    }

    private void append(StringBuilder where, List<Object> args, String name, String value) {
        if (validator.isEmpty(value))
            return;

        if (!args.isEmpty())
            where.append(" and ");
        where.append(name).append(" like ?");
        args.add(dataSource.getDialect(null).getLike(value, true, true));
    }

    @Override
    public ChromeModel findByKey(String key) {
        return liteOrm.findOne(new LiteQuery(ChromeModel.class).where("c_key=?"), new Object[]{key});
    }

    @Override
    public void save(ChromeModel chrome) {
        liteOrm.save(chrome);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(ChromeModel.class, id);
    }
}
