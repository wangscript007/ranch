package org.lpw.ranch.group.member;

import org.lpw.ranch.group.GroupService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MemberModel.NAME + ".ctrl")
@Execute(name = "/group/member/", key = MemberModel.NAME, code = "17")
public class MemberCtrl {
    @Inject
    private Request request;
    @Inject
    private MemberService memberService;

    @Execute(name = "join", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "reason", failureCode = 21),
            @Validate(validator = GroupService.VALIDATOR_EXISTS, parameter = "group", failureCode = 22),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object join() {
        memberService.join(request.get("group"), request.get("reason"));

        return "";
    }
}
