package org.lpw.ranch.paypal;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PaypalService.VALIDATOR_NOT_EXISTS)
public class NotExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private PaypalService paypalService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        PaypalModel paypal = paypalService.findByAppId(parameters[1]);

        return paypal == null || paypal.getKey().equals(parameters[0]);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return PaypalModel.NAME + ".exists";
    }
}
