package org.lpw.ranch.comment;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.audit.AuditService;

import java.sql.Timestamp;

/**
 * @author lpw
 */
public interface CommentService extends AuditService {
    /**
     * 检索评论信息集。
     *
     * @param audit  审核状态。
     * @param owner  所有者ID值。
     * @param author 作者ID值。
     * @param start  开始时间。
     * @param end    结束时间。
     * @return 评论信息集。
     */
    JSONObject query(int audit, String owner, String author, Timestamp start, Timestamp end);

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
    JSONObject create(CommentModel comment);
}
