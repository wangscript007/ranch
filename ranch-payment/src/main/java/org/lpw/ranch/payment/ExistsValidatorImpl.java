package org.lpw.ranch.payment;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PaymentService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private PaymentService paymentService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return paymentService.find(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return PaymentModel.NAME + ".not-exists";
    }
}
