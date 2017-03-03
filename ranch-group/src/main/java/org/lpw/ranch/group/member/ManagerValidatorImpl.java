package org.lpw.ranch.group.member;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MemberService.VALIDATOR_MANAGER)
public class ManagerValidatorImpl extends ManagerValidatorSupport {
    @Inject
    private UserHelper userHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return validate(memberService.findById(parameter).getGroup(), userHelper.id());
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return MemberModel.NAME + ".not-manager";
    }
}
