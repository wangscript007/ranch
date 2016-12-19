package org.lpw.ranch.user;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(UserService.VALIDATOR_SIGN_IN)
public class SignInValidatorImpl extends ValidatorSupport {
    @Autowired
    protected UserService userService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        return userService.signIn(parameters[0], parameters[1], parameters[2]);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return UserModel.NAME + ".sign-in.failure";
    }
}
