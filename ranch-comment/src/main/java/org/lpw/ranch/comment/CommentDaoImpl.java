package org.lpw.ranch.comment;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.recycle.Recycle;
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
@Repository(CommentModel.NAME + ".dao")
class CommentDaoImpl implements CommentDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<CommentModel> query(Audit audit, String owner, String author, Timestamp start, Timestamp end, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder(Recycle.No.getSql()).append(" and ").append(audit.getSql());
        List<Object> args = new ArrayList<>();
        if (start != null) {
            where.append(" and c_time>=?");
            args.add(start);
        }
        if (end != null) {
            where.append(" and c_time<=?");
            args.add(end);
        }
        if (!validator.isEmpty(owner)) {
            where.append(" and c_owner=?");
            args.add(owner);
        }
        if (!validator.isEmpty(author)) {
            where.append(" and c_author=?");
            args.add(author);
        }

        return liteOrm.query(new LiteQuery(CommentModel.class).where(where.toString()).order("c_time desc").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<CommentModel> query(Audit audit, String owner, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(CommentModel.class).where(Recycle.No.getSql() + " and " + audit.getSql() + " and c_owner=?").order("c_time desc")
                .size(pageSize).page(pageNum), new Object[]{owner});
    }

    @Override
    public PageList<CommentModel> query(String author, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(CommentModel.class).where(Recycle.No.getSql() + " and c_author=?").order("c_time desc")
                .size(pageSize).page(pageNum), new Object[]{author});
    }

    @Override
    public CommentModel findById(String id) {
        return liteOrm.findById(CommentModel.class, id);
    }

    @Override
    public void save(CommentModel comment) {
        liteOrm.save(comment);
    }
}
