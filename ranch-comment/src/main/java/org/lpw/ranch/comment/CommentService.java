package org.lpw.ranch.comment;

/**
 * @author lpw
 */
public interface CommentService {
    /**
     * 创建新评论。
     *
     * @param comment 评论信息。
     * @return 评论实例。
     */
    CommentModel create(CommentModel comment);
}
