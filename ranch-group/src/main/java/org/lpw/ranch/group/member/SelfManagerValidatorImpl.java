package org.lpw.ranch.group.member;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(MemberService.VALIDATOR_SELF_MANAGER)
public class SelfManagerValidatorImpl extends ManagerValidatorSupport {
    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        MemberModel member = memberService.findById(parameter);
        String user = userHelper.id();

        return member.getUser().equals(user) || validate(member.getGroup(), user);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return MemberModel.NAME + ".not-self-manager";
    }
}
