package org.lpw.ranch.comment;

import org.lpw.ranch.audit.AuditService;

/**
 * @author lpw
 */
public interface CommentService extends AuditService {
    /**
     * 创建新评论。
     *
     * @param comment 评论信息。
     * @return 评论实例。
     */
    CommentModel create(CommentModel comment);
}
