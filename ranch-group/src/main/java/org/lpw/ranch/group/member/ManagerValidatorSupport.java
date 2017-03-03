package org.lpw.ranch.group.member;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;

import javax.inject.Inject;

/**
 * @author lpw
 */
public abstract class ManagerValidatorSupport extends ValidatorSupport {
    @Inject
    UserHelper userHelper;
    @Inject
    MemberService memberService;

    protected boolean validate(String group, String user) {
        MemberModel manager = memberService.find(group, user);

        return manager != null && manager.getType() >= MemberService.Type.Manager.ordinal();
    }
}
