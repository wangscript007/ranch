package org.lpw.ranch.classify;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ClassifyService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private ClassifyService classifyService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return classifyService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return ClassifyModel.NAME + ".not-exists";
    }
}
