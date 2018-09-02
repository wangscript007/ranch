package org.lpw.ranch.push;

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
@Controller(PushModel.NAME + ".ctrl")
@Execute(name = "/push/", key = PushModel.NAME, code = "31")
public class PushCtrl {
    @Inject
    private Request request;
    @Inject
    private PushService pushService;

    @Execute(name = "query", validates = {
            @Validate(validator = PushService.VALIDATOR_EXISTS_SENDER, emptyable = true, parameter = "sender", failureCode = 4),
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return pushService.query(request.get("key"), request.get("sender"), request.get("subject"), request.get("template"),
                request.getAsInt("state", -1));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.ID, emptyable = true, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 3),
            @Validate(validator = PushService.VALIDATOR_EXISTS_SENDER, parameter = "sender", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appCode", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "subject", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "template", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 8),
            @Validate(validator = Validators.BETWEEN, number = {0, 1}, parameter = "state", failureCode = 9),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        return pushService.save(request.get("id"), request.get("key"), request.get("sender"), request.get("appCode"), request.get("subject"),
                request.get("content"), request.get("template"), request.get("name"), request.get("args"), request.getAsInt("state"));
    }

    @Execute(name = "state", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 1}, parameter = "state", failureCode = 9),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = PushService.VALIDATOR_EXISTS, parameter = "id", failureCode = 10)
    })
    public Object state() {
        return pushService.state(request.get("id"), request.getAsInt("state"));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = PushService.VALIDATOR_EXISTS, parameter = "id", failureCode = 10)
    })
    public Object delete() {
        pushService.delete(request.get("id"));

        return "";
    }

    @Execute(name = "send", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "receiver", failureCode = 10),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS, parameter = "user", failureCode = 11),
            @Validate(validator = PushService.VALIDATOR_EXISTS_KEY, parameter = "key", failureCode = 12)
    })
    public Object send() {
        return pushService.send(request.get("key"), request.get("user"), request.get("receiver"), request.getAsJsonObject("args"));
    }
}
