package org.lpw.ranch.push;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
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
@Repository(PushModel.NAME + ".dao")
class PushDaoImpl implements PushDao {
    @Inject
    private Validator validator;
    @Inject
    private DataSource dataSource;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<PushModel> query(String key, String sender, String subject, String template, int state, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_sender", DaoOperation.Equals, sender);
        daoHelper.where(where, args, "c_template", DaoOperation.Equals, template);
        daoHelper.where(where, args, "c_state", DaoOperation.Equals, state);
        daoHelper.like(null, where, args, "c_key", key);
        daoHelper.like(null, where, args, "c_subject", subject);

        return liteOrm.query(new LiteQuery(PushModel.class).where(where.toString())
                .order("c_state desc,c_time desc").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PushModel findById(String id) {
        return liteOrm.findById(PushModel.class, id);
    }

    @Override
    public PushModel find(String key, int state) {
        return liteOrm.findOne(new LiteQuery(PushModel.class).where("c_key=? and c_state=?"), new Object[]{key, state});
    }

    @Override
    public void save(PushModel push) {
        liteOrm.save(push);
    }

    @Override
    public void state(String key, int oldState, int newState) {
        liteOrm.update(new LiteQuery(PushModel.class).set("c_state=?").where("c_key=? and c_state=?"), new Object[]{newState, key, oldState});
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(PushModel.class, id);
    }
}
