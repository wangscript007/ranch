package org.lpw.ranch.comment;

import org.lpw.ranch.audit.AuditCtrlSupport;
import org.lpw.ranch.audit.AuditService;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(CommentModel.NAME + ".ctrl")
@Execute(name = "/comment/", key = CommentModel.NAME, code = "13")
public class CommentCtrl extends AuditCtrlSupport {
    @Autowired
    protected Request request;
    @Autowired
    protected CommentService commentService;

    /**
     * 检索评论集。
     * audit 审核状态。
     * pageSize 每页显示记录数。
     * pageNum 当前显示页数。
     *
     * @return {PageList}。
     */
    @Execute(name = "query", validates = {
            @Validate(validator = Validators.BETWEEN, number = {0, 2}, parameter = "audit", failureCode = 9),
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return commentService.query(request.getAsInt("audit"));
    }

    /**
     * 检索评论集。
     * owner 所有者ID。
     * pageSize 每页显示记录数。
     * pageNum 当前显示页数。
     *
     * @return {PageList}。
     */
    @Execute(name = "query-by-owner", validates = {
            @Validate(validator = Validators.ID, parameter = "owner", failureCode = 3)
    })
    public Object queryByOwner() {
        return commentService.queryByOwner(request.get("owner"));
    }

    /**
     * 检索评论集。
     * owner 作者ID。
     * pageSize 每页显示记录数。
     * pageNum 当前显示页数。
     *
     * @return {PageList}。
     */
    @Execute(name = "query-by-author", validates = {
            @Validate(validator = Validators.ID, parameter = "author", failureCode = 4)
    })
    public Object queryByAuthor() {
        return commentService.queryByAuthor(request.get("author"));
    }

    /**
     * 创建新回复。
     * key 服务key。
     * owner 所有者ID。
     * author 作者ID。
     * subject 标题。
     * label 标签。
     * content 内容。
     * score 评分。
     *
     * @return {CommentModel}。
     */
    @Execute(name = "create", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.ID, parameter = "owner", failureCode = 3),
            @Validate(validator = Validators.ID, parameter = "author", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "subject", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "label", failureCode = 6),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "content", failureCode = 7),
            @Validate(validator = Validators.BETWEEN, number = {0, 5}, parameter = "score", failureCode = 8)
    })
    public Object create() {
        return commentService.create(request.setToModel(new CommentModel()));
    }

    @Override
    protected AuditService getAuditService() {
        return commentService;
    }
}
