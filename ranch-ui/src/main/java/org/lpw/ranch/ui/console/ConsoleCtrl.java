package org.lpw.ranch.ui.console;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ConsoleModel.NAME + ".ctrl")
@Execute(name = "/ui/console/", key = ConsoleModel.NAME, code = "191")
public class ConsoleCtrl {
    @Inject
    private Validator validator;
    @Inject
    private Request request;
    @Inject
    private ConsoleService consoleService;

    @Execute(name = "sign-up", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "domain", failureCode = 1)
    })
    public Object signUp() {
        return consoleService.signUp(request.get("domain"));
    }

    @Execute(name = "menu", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "domain", failureCode = 1),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object menu() {
        return consoleService.menus(request.get("domain"));
    }

    @Execute(name = "meta", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "domain", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2)
    })
    public Object meta() {
        return consoleService.meta(request.get("domain"), request.get("key"));
    }

    @Execute(name = "service", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = ConsoleService.VALIDATOR_PERMIT, parameter = "domain", failureCode = 3)
    })
    public Object service() {
        return consoleService.service(request.get("key"), request.getMap());
    }
}
