package org.lpw.ranch.classify;

import org.lpw.ranch.model.Recycle;
import org.lpw.tephra.dao.jdbc.DataSource;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author lpw
 */
@Repository(ClassifyModel.NAME + ".dao")
class ClassifyDaoImpl implements ClassifyDao {
    @Autowired
    protected DataSource dataSource;
    @Autowired
    protected LiteOrm liteOrm;

    @Override
    public PageList<ClassifyModel> query(int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(ClassifyModel.class).where(Recycle.No.getWhere()).order("c_code").size(pageSize).page(pageNum), null);
    }

    @Override
    public PageList<ClassifyModel> query(String code, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(ClassifyModel.class).where(Recycle.No.getWhere() + " and c_code like ?").order("c_code").size(pageSize).page(pageNum),
                new Object[]{dataSource.getDialect(null).getLike(code, false, true)});
    }

    @Override
    public ClassifyModel findById(String id) {
        return liteOrm.findById(ClassifyModel.class, id);
    }

    @Override
    public void save(ClassifyModel classify) {
        liteOrm.save(classify);
    }
}
