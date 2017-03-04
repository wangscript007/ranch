package org.lpw.ranch.message;

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
@Controller(MessageModel.NAME + ".ctrl")
@Execute(name = "/message/", key = MessageModel.NAME, code = "18")
public class MessageCtrl {
    @Inject
    private Request request;
    @Inject
    private MessageService messageService;

    @Execute(name = "send", validates = {
            @Validate(validator = Validators.BETWEEN, number = {0, 1}, parameter = "type", failureCode = 1),
            @Validate(validator = Validators.ID, parameter = "receiver", failureCode = 2),
            @Validate(validator = Validators.BETWEEN, number = {0, 5}, parameter = "format", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "content", failureCode = 4),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object send() {
        messageService.send(request.getAsInt("type"), request.get("receiver"), request.getAsInt("format"), request.get("content"));

        return "";
    }
}
