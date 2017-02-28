package org.lpw.ranch.classify;

import org.lpw.ranch.recycle.Recycle;
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
@Repository(ClassifyModel.NAME + ".dao")
class ClassifyDaoImpl implements ClassifyDao {
    @Inject
    private Validator validator;
    @Inject
    private DataSource dataSource;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<ClassifyModel> query(int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(ClassifyModel.class).where(Recycle.No.getSql()).order("c_code").size(pageSize).page(pageNum), null);
    }

    @Override
    public PageList<ClassifyModel> query(String code, String key, String value, String name, int pageSize, int pageNum) {
        StringBuilder sql = new StringBuilder().append(Recycle.No.getSql()).append(" and c_code like ?");
        List<Object> args = new ArrayList<>();
        args.add(dataSource.getDialect(null).getLike(code, false, true));
        if (!validator.isEmpty(key)) {
            sql.append(" and c_key like ?");
            args.add(dataSource.getDialect(null).getLike(key, true, true));
        }
        if (!validator.isEmpty(value)) {
            sql.append(" and c_value like ?");
            args.add(dataSource.getDialect(null).getLike(value, true, true));
        }
        if (!validator.isEmpty(name)) {
            sql.append(" and c_name like ?");
            args.add(dataSource.getDialect(null).getLike(name, true, true));
        }

        return liteOrm.query(new LiteQuery(ClassifyModel.class).where(sql.toString()).order("c_code").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public ClassifyModel findById(String id) {
        return liteOrm.findById(ClassifyModel.class, id);
    }

    @Override
    public ClassifyModel findByCodeValue(String code, String value) {
        return liteOrm.findOne(new LiteQuery(ClassifyModel.class).where(Recycle.No.getSql() + " and c_code=? and c_value=?"), new Object[]{code, value});
    }

    @Override
    public void save(ClassifyModel classify) {
        liteOrm.save(classify);
    }
}
