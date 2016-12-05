package org.lpw.ranch.comment;

import org.lpw.ranch.audit.Audit;

/**
 * @author lpw
 */
interface CommentDao {
    void save(CommentModel comment);

    void audit(String[] ids, Audit audit);
}
