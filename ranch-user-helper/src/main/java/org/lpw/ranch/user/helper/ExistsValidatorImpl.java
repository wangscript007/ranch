package org.lpw.ranch.user.helper;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserHelper.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return userHelper.get(parameter).size() > 1;
    }

    @Override
    public int getFailureCode(ValidateWrapper validate) {
        return 1525;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.user.helper.not-exists";
    }
}
