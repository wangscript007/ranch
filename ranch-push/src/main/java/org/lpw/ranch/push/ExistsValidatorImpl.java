package org.lpw.ranch.push;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PushService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private PushService pushService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return pushService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return PushModel.NAME + ".not-exists";
    }
}
