package org.lpw.ranch.google;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(GoogleModel.NAME + ".ctrl")
@Execute(name = "/google/", key = GoogleModel.NAME, code = "145")
public class GoogleCtrl {
    @Inject
    private Request request;
    @Inject
    private GoogleService googleService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return googleService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "clientId", failureCode = 4),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        googleService.save(request.setToModel(GoogleModel.class));

        return "";
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 8),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        googleService.delete(request.get("id"));

        return "";
    }

    @Execute(name = "auth", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "token", failureCode = 10),
            @Validate(validator = GoogleService.VALIDATOR_KEY_EXISTS, parameter = "key", failureCode = 13)
    })
    public Object auth() {
        return googleService.auth(request.get("key"), request.get("token"));
    }
}
