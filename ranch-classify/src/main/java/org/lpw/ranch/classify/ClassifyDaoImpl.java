package org.lpw.ranch.classify;

import org.lpw.ranch.recycle.Recycle;
import org.lpw.ranch.util.DaoHelper;
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
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<ClassifyModel> query(int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(ClassifyModel.class).where(Recycle.No.getSql()).order("c_code,c_key")
                .size(pageSize).page(pageNum), null);
    }

    @Override
    public PageList<ClassifyModel> query(String code, String key, String value, String name, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder().append(Recycle.No.getSql());
        List<Object> args = new ArrayList<>();
        daoHelper.like(null, where, args, "c_code", code, false, true, true);
        daoHelper.like(null, where, args, "c_key", key);
        daoHelper.like(null, where, args, "c_value", value);
        daoHelper.like(null, where, args, "c_name", name);

        return liteOrm.query(new LiteQuery(ClassifyModel.class).where(where.toString()).order("c_code,c_key")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<ClassifyModel> query(String code) {
        return liteOrm.query(new LiteQuery(ClassifyModel.class).where(Recycle.No.getSql() + " and c_code=?"), new Object[]{code});
    }

    @Override
    public ClassifyModel findById(String id) {
        return liteOrm.findById(ClassifyModel.class, id);
    }

    @Override
    public ClassifyModel findByCodeKey(String code, String key) {
        return liteOrm.findOne(new LiteQuery(ClassifyModel.class).where(Recycle.No.getSql() + " and c_code=? and c_key=?"),
                new Object[]{code, key});
    }

    @Override
    public void save(ClassifyModel classify) {
        liteOrm.save(classify);
    }
}
