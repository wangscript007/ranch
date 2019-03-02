package org.lpw.ranch.weixin.reply;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ReplyModel.NAME + ".ctrl")
@Execute(name = "/weixin/reply/", key = ReplyModel.NAME, code = "24")
public class ReplyCtrl {
    @Inject
    private Request request;
    @Inject
    private ReplyService replyService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN, string = {"ranch-weixin"})
    })
    public Object query() {
        return replyService.query(request.get("key"), request.get("receiveType"), request.get("receiveMessage"),
                request.getAsInt("state", -1));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 21),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 22),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "receiveType", failureCode = 23),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "receiveType", failureCode = 24),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "receiveMessage", failureCode = 25),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "receiveMessage", failureCode = 26),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "sendType", failureCode = 27),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "sendType", failureCode = 28),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "sendMessage", failureCode = 29),
            @Validate(validator = Validators.SIGN, string = {"ranch-weixin"})
    })
    public Object save() {
        replyService.save(request.setToModel(ReplyModel.class));

        return "";
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.SIGN, string = {"ranch-weixin"})
    })
    public Object delete() {
        replyService.delete(request.get("id"));

        return "";
    }
}
