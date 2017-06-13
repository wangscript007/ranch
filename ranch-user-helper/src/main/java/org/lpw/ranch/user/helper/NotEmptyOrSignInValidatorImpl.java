package org.lpw.ranch.user.helper;

import org.lpw.tephra.ctrl.validate.IdValidatorSupport;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserHelper.VALIDATOR_NOT_EMPTY_OR_SIGN_IN)
public class NotEmptyOrSignInValidatorImpl extends IdValidatorSupport {
    @Inject
    private UserHelper userHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        if (validator.isEmpty(parameter))
            return !userHelper.sign().isEmpty();

        return parameter.length() <= 36;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.user.helper.empty-and-not-sign-in";
    }
}
