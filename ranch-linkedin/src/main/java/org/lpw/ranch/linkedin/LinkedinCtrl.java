package org.lpw.ranch.linkedin;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(LinkedinModel.NAME + ".ctrl")
@Execute(name = "/linkedin/", key = LinkedinModel.NAME, code = "40")
public class LinkedinCtrl {
    @Inject
    private Request request;
    @Inject
    private LinkedinService linkedinService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return linkedinService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "appId", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appId", failureCode = 5),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "secret", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "secret", failureCode = 7),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        linkedinService.save(request.setToModel(LinkedinModel.class));

        return "";
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 11),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        linkedinService.delete(request.get("id"));

        return "";
    }

    @Execute(name = "auth", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "code", failureCode = 10),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "redirectUri", failureCode = 12),
            @Validate(validator = LinkedinService.VALIDATOR_EXISTS_KEY, parameter = "key", failureCode = 13)
    })
    public Object auth() {
        return linkedinService.auth(request.get("key"), request.get("code"), request.get("redirectUri"));
    }
}
