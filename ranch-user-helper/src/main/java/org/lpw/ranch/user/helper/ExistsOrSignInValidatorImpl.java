package org.lpw.ranch.user.helper;

import org.lpw.tephra.ctrl.validate.IdValidatorSupport;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserHelper.VALIDATOR_EXISTS_OR_SIGN_IN)
public class ExistsOrSignInValidatorImpl extends IdValidatorSupport {
    @Inject
    private UserHelper userHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return validator.isEmpty(parameter) ? userHelper.signIn() : userHelper.exists(parameter);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.user.helper.not-exists-and-not-sign-in";
    }
}
