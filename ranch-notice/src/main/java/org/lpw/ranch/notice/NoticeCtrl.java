package org.lpw.ranch.notice;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(NoticeModel.NAME + ".ctrl")
@Execute(name = "/notice/", key = NoticeModel.NAME, code = "36")
public class NoticeCtrl {
    @Inject
    private Request request;
    @Inject
    private NoticeService noticeService;

    @Execute(name = "query", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object query() {
        return noticeService.query(request.get("type"), request.getAsInt("read", -1));
    }

    @Execute(name = "send", validates = {
            @Validate(validator = Validators.ID, parameter = "user", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "subject", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "content", failureCode = 4),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS, parameter = "user", failureCode = 5)
    })
    public Object send() {
        noticeService.send(request.get("user"), request.get("type"), request.get("subject"), request.get("content"));

        return "";
    }

    @Execute(name = "sends", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "users", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "subject", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "content", failureCode = 4),
            @Validate(validator = Validators.SIGN)
    })
    public Object sends() {
        noticeService.send(request.getAsArray("users"), request.get("type"), request.get("subject"), request.get("content"));

        return "";
    }

    @Execute(name = "read")
    public Object read() {
        noticeService.read(request.get("id"));

        return "";
    }

    @Execute(name = "reads")
    public Object reads() {
        noticeService.reads(request.get("type"));

        return "";
    }
}
