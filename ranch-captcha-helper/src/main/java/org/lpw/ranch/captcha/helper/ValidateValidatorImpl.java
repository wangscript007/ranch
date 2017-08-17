package org.lpw.ranch.captcha.helper;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(CaptchaHelper.VALIDATOR_VALIDATE)
public class ValidateValidatorImpl extends ValidatorSupport {
    @Inject
    private CaptchaHelper captchaHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return captchaHelper.validate(validate.getString()[0], parameter);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.captcha.helper.validate.failure";
    }
}
