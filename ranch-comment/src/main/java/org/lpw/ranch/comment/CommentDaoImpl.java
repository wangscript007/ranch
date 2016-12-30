package org.lpw.ranch.comment;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditDao;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(CommentModel.NAME + ".dao")
class CommentDaoImpl implements CommentDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private AuditDao auditDao;

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
    public CommentModel findById(String id) {
        return liteOrm.findById(CommentModel.class, id);
    }

    @Override
    public void save(CommentModel comment) {
        liteOrm.save(comment);
    }

    @Override
    public void audit(String[] ids, Audit audit, String auditRemark) {
        auditDao.audit(CommentModel.class, ids, audit, auditRemark);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(CommentModel.class, id);
    }
}
