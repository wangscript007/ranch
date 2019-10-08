package org.lpw.ranch.facebook;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(FacebookModel.NAME + ".ctrl")
@Execute(name = "/facebook/", key = FacebookModel.NAME, code = "139")
public class FacebookCtrl {
    @Inject
    private Request request;
    @Inject
    private FacebookService facebookService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return facebookService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "appId", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appId", failureCode = 5),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "secret", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "secret", failureCode = 7),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "version", failureCode = 8),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "version", failureCode = 9),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        facebookService.save(request.setToModel(FacebookModel.class));

        return "";
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 11),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        facebookService.delete(request.get("id"));

        return "";
    }

    @Execute(name = "auth", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "code", failureCode = 10),
            @Validate(validator = FacebookService.VALIDATOR_EXISTS_KEY, parameter = "key", failureCode = 12)
    })
    public Object auth() {
        return facebookService.auth(request.get("key"), request.get("code"));
    }
}
