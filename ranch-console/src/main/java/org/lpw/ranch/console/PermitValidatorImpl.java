package org.lpw.ranch.console;

import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(ConsoleService.VALIDATOR_PERMIT)
public class PermitValidatorImpl extends ValidatorSupport {
    @Override
    protected String getDefaultFailureMessageKey() {
        return ConsoleModel.NAME + ".not-permit";
    }
}
