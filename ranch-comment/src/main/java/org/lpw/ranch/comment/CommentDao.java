package org.lpw.ranch.comment;

import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface CommentDao {
    PageList<CommentModel> query(Audit audit, int pageSize, int pageNum);

    PageList<CommentModel> query(Audit audit, String owner, int pageSize, int pageNum);

    PageList<CommentModel> query(String author, int pageSize, int pageNum);

    CommentModel findById(String id);

    void save(CommentModel comment);

    void audit(String[] ids, Audit audit, String auditRemark);

    void delete(String id);
}
