package org.lpw.ranch.user.online;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
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
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<OnlineModel> query(String user, String ip, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_ip", DaoOperation.Equals, ip);

        return liteOrm.query(new LiteQuery(OnlineModel.class).where(where.toString()).order("c_last_visit desc")
                .size(pageSize).page(pageNum), args.toArray());
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
