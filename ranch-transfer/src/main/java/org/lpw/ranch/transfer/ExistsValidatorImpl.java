package org.lpw.ranch.transfer;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(TransferService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private TransferService transferService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return transferService.find(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return TransferModel.NAME + ".not-exists";
    }
}
