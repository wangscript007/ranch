package org.lpw.ranch.user.crosier;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(CrosierModel.NAME + ".dao")
class CrosierDaoImpl implements CrosierDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<CrosierModel> query(int grade) {
        return liteOrm.query(new LiteQuery(CrosierModel.class).where("c_grade=?"), new Object[]{grade});
    }

    @Override
    public void save(CrosierModel crosier) {
        liteOrm.save(crosier);
    }

    @Override
    public void delete(int grade) {
        liteOrm.delete(new LiteQuery(CrosierModel.class).where("c_grade=?"), new Object[]{grade});
    }
}
