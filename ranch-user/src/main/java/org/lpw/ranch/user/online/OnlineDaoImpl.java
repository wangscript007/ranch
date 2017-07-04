package org.lpw.ranch.user.online;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(OnlineModel.NAME + ".dao")
class OnlineDaoImpl implements OnlineDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<OnlineModel> query(String user, String ip, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        append(where, args, "c_user", user);
        append(where, args, "c_ip", ip);

        return liteOrm.query(new LiteQuery(OnlineModel.class).where(where.toString()).order("c_last_visit desc").size(pageSize).page(pageNum), args.toArray());
    }

    private void append(StringBuilder where, List<Object> args, String name, Object value) {
        if (validator.isEmpty(value))
            return;

        if (args.size() > 0)
            where.append(" and ");
        where.append(name).append("=?");
        args.add(value);
    }

    @Override
    public PageList<OnlineModel> query(Timestamp visit) {
        return liteOrm.query(new LiteQuery(OnlineModel.class).where("c_last_visit<?"), new Object[]{visit});
    }

    @Override
    public OnlineModel findBySid(String sid) {
        return liteOrm.findOne(new LiteQuery(OnlineModel.class).where("c_sid=?"), new Object[]{sid});
    }

    @Override
    public void save(OnlineModel online) {
        liteOrm.save(online);
    }

    @Override
    public void delete(OnlineModel online) {
        liteOrm.delete(online);
    }
}
