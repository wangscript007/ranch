package org.lpw.ranch.weixin;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(WeixinService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private WeixinService weixinService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return weixinService.findByKey(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return WeixinModel.NAME + ".not-exists";
    }
}
