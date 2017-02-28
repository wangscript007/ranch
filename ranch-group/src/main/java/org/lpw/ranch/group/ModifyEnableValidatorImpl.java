package org.lpw.ranch.group;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(GroupService.VALIDATOR_MODIFY_ENABLE)
public class ModifyEnableValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private GroupService groupService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        String owner = groupService.get(parameter).getString("owner");

        return owner != null && owner.equals(userHelper.id());
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return GroupModel.NAME + ".modify.disable";
    }
}
