package org.lpw.ranch.comment;

import org.lpw.ranch.audit.AuditCtrlSupport;
import org.lpw.ranch.audit.AuditService;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(CommentModel.NAME + ".ctrl")
@Execute(name = "/comment/", key = CommentModel.NAME, code = "13")
public class CommentCtrl extends AuditCtrlSupport {
    @Inject
    private DateTime dateTime;
    @Inject
    private Request request;
    @Inject
    private CommentService commentService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.BETWEEN, number = {0, 2}, parameter = "audit", failureCode = 9),
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return commentService.query(request.getAsInt("audit"), request.get("owner"), request.get("author"), dateTime.getStart(request.get("start")), dateTime.getEnd(request.get("end")));
    }

    @Execute(name = "query-by-owner", validates = {
            @Validate(validator = Validators.ID, parameter = "owner", failureCode = 3)
    })
    public Object queryByOwner() {
        return commentService.queryByOwner(request.get("owner"));
    }

    @Execute(name = "query-by-author", validates = {
            @Validate(validator = Validators.ID, parameter = "author", failureCode = 4)
    })
    public Object queryByAuthor() {
        return commentService.queryByAuthor(request.get("author"));
    }

    @Execute(name = "get", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 9)
    })
    public Object get() {
        return commentService.get(request.getAsArray("ids"));
    }

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
