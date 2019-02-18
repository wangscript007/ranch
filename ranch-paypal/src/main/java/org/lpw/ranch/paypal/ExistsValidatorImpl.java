package org.lpw.ranch.paypal;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PaypalService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private PaypalService paypalService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return paypalService.findByKey(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return PaypalModel.NAME + ".not-exists";
    }
}
