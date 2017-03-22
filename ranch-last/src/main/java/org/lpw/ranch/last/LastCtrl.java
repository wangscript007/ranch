package org.lpw.ranch.last;

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
@Controller(LastModel.NAME + ".ctrl")
@Execute(name = "/last/", key = LastModel.NAME, code = "20")
public class LastCtrl {
    @Inject
    private Request request;
    @Inject
    private LastService lastService;

    @Execute(name = "find", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 2),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object find() {
        return lastService.find(request.get("type"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 2),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object save() {
        return lastService.save(request.get("type"));
    }
}
