package org.lpw.ranch.console;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ConsoleService.VALIDATOR_PERMIT)
public class PermitValidatorImpl extends ValidatorSupport {
    @Inject
    private ConsoleService consoleService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return consoleService.permit();
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return ConsoleModel.NAME + ".not-permit";
    }
}
