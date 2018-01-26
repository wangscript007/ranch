package org.lpw.ranch.chrome;

import org.lpw.ranch.util.DaoHelper;
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
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<ChromeModel> query(String key, String name, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.like(null, where, args, "c_key", key);
        daoHelper.like(null, where, args, "c_name", name);

        return liteOrm.query(new LiteQuery(ChromeModel.class).where(where.toString()).size(pageSize).page(pageNum), args.toArray());
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
