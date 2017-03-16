package org.lpw.ranch.message;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Message;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MessageModel.NAME + ".ctrl")
@Execute(name = "/message/", key = MessageModel.NAME, code = "18")
public class MessageCtrl {
    @Inject
    private Message message;
    @Inject
    private Request request;
    @Inject
    private Templates templates;
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
        boolean result = request.getAsInt("type") == 1 ? messageService.sendToGroup(request.get("receiver"), request.getAsInt("format"), request.get("content")) :
                messageService.sendToFriend(request.get("receiver"), request.getAsInt("format"), request.get("content"));

        return result ? "" : templates.get().failure(1805, message.get(MessageModel.NAME + ".send.failure"), null, null);
    }

    @Execute(name = "newest", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object newest() {
        return messageService.newest(request.getAsLong("time"));
    }
}
