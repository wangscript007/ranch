package org.lpw.ranch.notice;

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
@Repository(NoticeModel.NAME + ".dao")
class NoticeDaoImpl implements NoticeDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<NoticeModel> query(String user, String type) {
        return liteOrm.query(new LiteQuery(NoticeModel.class).where("c_user=? and c_type=?").order("c_time desc"), new Object[]{user, type});
    }

    @Override
    public PageList<NoticeModel> query(String user, String type, String subject, int read, Timestamp[] time, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_read", DaoOperation.Equals, read);
        daoHelper.where(where, args, "c_time", DaoOperation.GreaterEquals, time[0]);
        daoHelper.where(where, args, "c_time", DaoOperation.LessEquals, time[1]);
        daoHelper.like(null, where, args, "c_subject", subject);

        return liteOrm.query(new LiteQuery(NoticeModel.class).where(where.toString())
                .order("c_time desc").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<NoticeModel> query(String user, String type, Timestamp time) {
        return time == null ? liteOrm.query(new LiteQuery(NoticeModel.class).where("c_user=? and c_type=?"), new Object[]{user, type})
                : liteOrm.query(new LiteQuery(NoticeModel.class).where("c_user=? and c_type=? and c_time>?"), new Object[]{user, type, time});
    }

    @Override
    public NoticeModel findById(String id) {
        return liteOrm.findById(NoticeModel.class, id);
    }

    @Override
    public NoticeModel find(String user, int marker) {
        return liteOrm.findOne(new LiteQuery(NoticeModel.class).where("c_user=? and c_marker=?").order("c_time desc"), new Object[]{user, marker});
    }

    @Override
    public void save(NoticeModel notice) {
        liteOrm.save(notice);
    }
}
