package org.lpw.ranch.user.helper;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserHelper.VALIDATOR_VIP)
public class VipValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return userHelper.isVip();
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.user.helper.not-vip";
    }
}
