package org.lpw.ranch.facebook;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(FacebookService.VALIDATOR_EXISTS_KEY)
public class ExistsKeyValidatorImpl extends ValidatorSupport {
    @Inject
    private FacebookService facebookService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return facebookService.findByKey(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return FacebookModel.NAME + ".key.not-exists";
    }
}
