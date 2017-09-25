package org.lpw.ranch.console;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lpw
 */
@Controller(ConsoleService.VALIDATOR_PERMIT)
public class PermitValidatorImpl extends ValidatorSupport {
    @Inject
    private ConsoleService consoleService;
    private Set<String> ignores;

    public PermitValidatorImpl() {
        ignores = new HashSet<>();
        ignores.add("ranch.user.sign-in");
        ignores.add("ranch.user.sign");
        ignores.add("ranch.user.sign-out");
    }

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return ignores.contains(parameter) || consoleService.permit();
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return ConsoleModel.NAME + ".not-permit";
    }
}
