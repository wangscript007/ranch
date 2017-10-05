package org.lpw.ranch.group.helper;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(GroupHelper.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private GroupHelper groupHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return groupHelper.get(parameter).size() > 1;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.group.helper.not-exists";
    }
}
