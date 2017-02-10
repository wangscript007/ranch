package org.lpw.ranch.user;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private UserService userService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return userService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return UserModel.NAME + ".not-exists";
    }
}
