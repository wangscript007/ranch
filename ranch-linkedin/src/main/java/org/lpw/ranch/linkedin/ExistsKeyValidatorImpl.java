package org.lpw.ranch.linkedin;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(LinkedinService.VALIDATOR_EXISTS_KEY)
public class ExistsKeyValidatorImpl extends ValidatorSupport {
    @Inject
    private LinkedinService linkedinService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return linkedinService.findByKey(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return LinkedinModel.NAME + ".key.not-exists";
    }
}
