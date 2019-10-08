package org.lpw.ranch.ui.console;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Header;
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
    private Header header;
    @Inject
    private Request request;
    @Inject
    private ConsoleService consoleService;

    @Execute(name = "sign-up", validates = {
            @Validate(validator = Validators.NOT_EMPTY, scope = Validate.Scope.Header, parameter = "domain", failureCode = 1)
    })
    public Object signUp() {
        return consoleService.signUp(header.get("domain"));
    }

    @Execute(name = "menu", validates = {
            @Validate(validator = Validators.NOT_EMPTY, scope = Validate.Scope.Header, parameter = "domain", failureCode = 1),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object menu() {
        return consoleService.menus(header.get("domain"));
    }

    @Execute(name = "meta", validates = {
            @Validate(validator = Validators.NOT_EMPTY, scope = Validate.Scope.Header, parameter = "domain", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, scope = Validate.Scope.Header, parameter = "key", failureCode = 2)
    })
    public Object meta() {
        return consoleService.meta(header.get("domain"), header.get("key"));
    }

    @Execute(name = "service", validates = {
            @Validate(validator = Validators.NOT_EMPTY, scope = Validate.Scope.Header, parameter = "key", failureCode = 2),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = ConsoleService.VALIDATOR_PERMIT, scope = Validate.Scope.Header, parameter = "domain", failureCode = 3)
    })
    public Object service() {
        return consoleService.service(header.get("key"), request.getMap());
    }
}
