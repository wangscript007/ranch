package org.lpw.ranch.notice;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
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
    public PageList<NoticeModel> query(String user, String type, int read, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_read", DaoOperation.Equals, read);

        return liteOrm.query(new LiteQuery(NoticeModel.class).where(where.toString())
                .order("c_time desc").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public NoticeModel findById(String id) {
        return liteOrm.findById(NoticeModel.class, id);
    }

    @Override
    public void save(NoticeModel notice) {
        liteOrm.save(notice);
    }
}
