package org.lpw.ranch.account;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AccountService.VALIDATOR_CONSUME)
public class ConsumeValidatorImpl extends ValidatorSupport {
    @Inject
    private AccountService accountService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return validate("", 0, converter.toInt(parameter));
    }

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        return validate(parameters[0], converter.toInt(parameters[1]), converter.toInt(parameters[2]));
    }

    private boolean validate(String owner, int type, int amount) {
        return amount > 0 && accountService.consume(owner, type, amount) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return AccountModel.NAME + ".consume.failure";
    }
}
