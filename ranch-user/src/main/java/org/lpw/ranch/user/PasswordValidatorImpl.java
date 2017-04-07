package org.lpw.ranch.user;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserService.VALIDATOR_PASSWORD)
public class PasswordValidatorImpl extends ValidatorSupport {
    @Inject
    private Request request;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return request.getAsInt("type") != UserService.Type.Self.ordinal() || !validator.isEmpty(parameter);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return UserModel.NAME + ".password.empty";
    }
}
