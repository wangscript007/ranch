package org.lpw.ranch.user;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(User.VALIDATOR_SIGN_IN)
public class SignInValidatorImpl extends ValidatorSupport {
    @Inject
    private User user;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return !user.sign().isEmpty();
    }

    @Override
    public int getFailureCode(ValidateWrapper validate) {
        return 9901;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.base.user.need-sign-in";
    }
}
