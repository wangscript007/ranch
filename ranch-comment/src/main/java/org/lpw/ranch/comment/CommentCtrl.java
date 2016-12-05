package org.lpw.ranch.comment;

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
public class CommentCtrl {
    @Autowired
    protected Request request;
    @Autowired
    protected CommentService commentService;

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
     * @return ""。
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
}
