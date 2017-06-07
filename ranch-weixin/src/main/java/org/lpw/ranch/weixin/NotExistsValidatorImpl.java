package org.lpw.ranch.weixin;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(WeixinService.VALIDATOR_NOT_EXISTS)
public class NotExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private WeixinService weixinService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        WeixinModel weixin = weixinService.findByAppId(parameters[1]);

        return weixin == null || weixin.getKey().equals(parameters[0]);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return WeixinModel.NAME + ".exists";
    }
}
