package org.lpw.ranch.group;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(GroupService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private GroupService groupService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return !groupService.get(parameter).isEmpty();
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return GroupModel.NAME + ".not-exists";
    }
}
