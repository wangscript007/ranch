package org.lpw.ranch.comment;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditDao;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author lpw
 */
@Repository(CommentModel.NAME + ".dao")
class CommentDaoImpl implements CommentDao {
    @Autowired
    protected LiteOrm liteOrm;
    @Autowired
    protected AuditDao auditDao;

    @Override
    public PageList<CommentModel> query(Audit audit, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(CommentModel.class).where(audit.getSql()).order("c_time desc").size(pageSize).page(pageNum), null);
    }

    @Override
    public PageList<CommentModel> query(Audit audit, String owner, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(CommentModel.class).where(audit.getSql() + " and c_owner=?").order("c_time desc")
                .size(pageSize).page(pageNum), new Object[]{owner});
    }

    @Override
    public PageList<CommentModel> query(String author, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(CommentModel.class).where("c_author=?").order("c_time desc")
                .size(pageSize).page(pageNum), new Object[]{author});
    }

    @Override
    public void save(CommentModel comment) {
        liteOrm.save(comment);
    }

    @Override
    public void audit(String[] ids, Audit audit) {
        auditDao.audit(CommentModel.class, ids, audit);
    }
}
