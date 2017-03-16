package org.lpw.ranch.comment;

import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface CommentDao {
    PageList<CommentModel> query(Audit audit, String owner, String author, Timestamp start, Timestamp end, int pageSize, int pageNum);

    PageList<CommentModel> query(Audit audit, String owner, int pageSize, int pageNum);

    PageList<CommentModel> query(String author, int pageSize, int pageNum);

    CommentModel findById(String id);

    void save(CommentModel comment);
}
