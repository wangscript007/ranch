package org.lpw.ranch.user;

import org.lpw.ranch.user.type.Types;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(UserService.VALIDATOR_PASSWORD)
public class PasswordValidatorImpl extends ValidatorSupport {
    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        return !validator.isEmpty(parameters[0]) || numeric.toInt(parameters[1]) != Types.SELF;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return UserModel.NAME + ".password.empty";
    }
}
