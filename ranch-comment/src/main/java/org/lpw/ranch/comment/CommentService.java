package org.lpw.ranch.comment;

import net.sf.json.JSONObject;
import org.lpw.ranch.audit.AuditService;

/**
 * @author lpw
 */
public interface CommentService extends AuditService {
    /**
     * 检索评论信息集。
     *
     * @param audit 审核状态。
     * @return 评论信息集。
     */
    JSONObject query(int audit);

    /**
     * 检索指定所有者的评论信息集。
     *
     * @param owner 所有者ID。
     * @return 评论信息集。
     */
    JSONObject queryByOwner(String owner);

    /**
     * 检索指定作者的评论信息集。
     *
     * @param author 作者ID。
     * @return 评论信息集。
     */
    JSONObject queryByAuthor(String author);

    /**
     * 创建新评论。
     *
     * @param comment 评论信息。
     * @return 评论实例。
     */
    CommentModel create(CommentModel comment);
}
