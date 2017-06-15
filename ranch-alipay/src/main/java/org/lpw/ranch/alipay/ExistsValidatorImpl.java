package org.lpw.ranch.alipay;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AlipayService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private AlipayService alipayService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return alipayService.findByKey(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return AlipayModel.NAME + ".not-exists";
    }
}
