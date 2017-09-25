package org.lpw.ranch.console;

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
@Execute(name = "/console/", key = ConsoleModel.NAME, code = "98")
public class ConsoleCtrl {
    @Inject
    private Validator validator;
    @Inject
    private Header header;
    @Inject
    private Request request;
    @Inject
    private ConsoleService consoleService;

    @Execute(name = "menu")
    public Object menu() {
        return consoleService.menus();
    }

    @Execute(name = "meta", validates = {
            @Validate(validator = Validators.NOT_EMPTY, scope = Validate.Scope.Header, parameter = "key", failureCode = 1)
    })
    public Object meta() {
        return consoleService.meta(header.get("key"));
    }

    @Execute(name = "service", validates = {
            @Validate(validator = Validators.NOT_EMPTY, scope = Validate.Scope.Header, parameter = "key", failureCode = 1),
            @Validate(validator = ConsoleService.VALIDATOR_PERMIT, scope = Validate.Scope.Header, parameter = "key", failureCode = 2)
    })
    public Object service() {
        return consoleService.service(header.get("key"), request.getMap());
    }
}
