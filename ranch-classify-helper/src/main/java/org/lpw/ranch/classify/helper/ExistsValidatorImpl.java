package org.lpw.ranch.classify.helper;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ClassifyHelper.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private ClassifyHelper classifyHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return !classifyHelper.get(parameter).getJSONObject(parameter).isEmpty();
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.classify.helper.not-exists";
    }
}
