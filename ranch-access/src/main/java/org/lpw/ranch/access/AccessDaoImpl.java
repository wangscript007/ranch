package org.lpw.ranch.access;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(AccessModel.NAME + ".dao")
class AccessDaoImpl implements AccessDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<AccessModel> query(String host, String uri, String user, String userAgent, Timestamp start, Timestamp end,
                                       int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_host", DaoOperation.Equals, host);
        daoHelper.like(null, where, args, "c_uri", uri, false, true);
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_time", DaoOperation.GreaterEquals, start);
        daoHelper.where(where, args, "c_time", DaoOperation.LessEquals, end);
        daoHelper.like(null, where, args, "c_user_agent", userAgent);

        return liteOrm.query(new LiteQuery(AccessModel.class).where(where.toString())
                .order("c_time desc").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public void save(AccessModel access) {
        liteOrm.save(access);
        liteOrm.close();
    }
}
