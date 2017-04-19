package org.lpw.ranch.user.helper;

import org.lpw.tephra.ctrl.validate.IdValidatorSupport;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserHelper.VALIDATOR_ID_OR_SIGN_IN)
public class IdOrSignInValidatorImpl extends IdValidatorSupport {
    @Inject
    private UserHelper userHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return super.validate(validate, parameter) || (validator.isEmpty(parameter) && !userHelper.sign().isEmpty());
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.user.helper.not-id-and-sign-in";
    }
}
