package org.lpw.ranch.weixin.helper;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(WeixinHelper.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private WeixinHelper weixinHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return !validator.isEmpty(parameter) && !validator.isEmpty(weixinHelper.getAppId(parameter));
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.weixin.helper.not-exists";
    }
}
