package org.lpw.ranch.milestone;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller(MilestoneService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private MilestoneService milestoneService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        return !validator.isEmpty(milestoneService.find(parameters[0], parameters[1]));
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return MilestoneModel.NAME + ".not-exists";
    }
}
