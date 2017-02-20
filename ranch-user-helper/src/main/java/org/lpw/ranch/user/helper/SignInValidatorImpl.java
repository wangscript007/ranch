package org.lpw.ranch.user.helper;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserHelper.VALIDATOR_SIGN_IN)
public class SignInValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return !userHelper.sign().isEmpty();
    }

    @Override
    public int getFailureCode(ValidateWrapper validate) {
        return 9901;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.user.helper.need-sign-in";
    }
}
