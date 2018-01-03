package org.lpw.ranch.push;

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

    @Override
    public PageList<PushModel> query(String key, String subject, int state, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        if (!validator.isEmpty(key)) {
            where.append("c_key like ?");
            args.add(dataSource.getDialect(null).getLike(key, true, true));
        }
        if (!validator.isEmpty(subject)) {
            if (!args.isEmpty())
                where.append(" and ");
            where.append("c_subject like ?");
            args.add(dataSource.getDialect(null).getLike(subject, true, true));
        }
        if (state > -1) {
            if (!args.isEmpty())
                where.append(" and ");
            where.append("c_state=?");
            args.add(state);
        }

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
