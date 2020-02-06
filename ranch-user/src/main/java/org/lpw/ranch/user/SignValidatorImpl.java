package org.lpw.ranch.user;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserService.VALIDATOR_SIGN)
public class SignValidatorImpl extends ValidatorSupport {
    @Inject
    private UserService userService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return !userService.sign().isEmpty();
    }

    @Override
    public int getFailureCode(ValidateWrapper validate) {
        return 999901;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return UserModel.NAME + ".need-sign-in";
    }
}
