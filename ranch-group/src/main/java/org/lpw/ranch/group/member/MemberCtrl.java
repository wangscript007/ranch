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

    @Execute(name = "query-by-group", validates = {
            @Validate(validator = Validators.ID, parameter = "group", failureCode = 28)
    })
    public Object queryByGroup() {
        return memberService.queryByGroup(request.get("group"));
    }

    @Execute(name = "find", validates = {
            @Validate(validator = MemberService.VALIDATOR_USER_EXISTS, parameters = {"group", "user"}, failureCode = 29)
    })
    public Object find() {
        return memberService.find(request.get("group"), request.get("user"));
    }

    @Execute(name = "join", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "reason", failureCode = 21),
            @Validate(validator = GroupService.VALIDATOR_EXISTS, parameter = "group", failureCode = 22),
            @Validate(validator = Validators.ID, emptyable = true, parameter = "introducer", failureCode = 28),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object join() {
        memberService.join(request.get("group"), request.get("reason"), request.get("introducer"));

        return "";
    }

    @Execute(name = "pass", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id", failureCode = 23),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = MemberService.VALIDATOR_EXISTS, parameter = "id", failureCode = 24),
            @Validate(validator = MemberService.VALIDATOR_MANAGER, parameter = "id", failureCode = 25)
    })
    public Object pass() {
        memberService.pass(request.get("id"));

        return "";
    }

    @Execute(name = "refuse", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id", failureCode = 23),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = MemberService.VALIDATOR_EXISTS, parameter = "id", failureCode = 24),
            @Validate(validator = MemberService.VALIDATOR_MANAGER, parameter = "id", failureCode = 25)
    })
    public Object refuse() {
        memberService.refuse(request.get("id"));

        return "";
    }

    @Execute(name = "manager", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id", failureCode = 23),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = MemberService.VALIDATOR_EXISTS, parameter = "id", failureCode = 24),
            @Validate(validator = MemberService.VALIDATOR_MANAGER, parameter = "id", failureCode = 25)
    })
    public Object manager() {
        memberService.manager(request.get("id"));

        return "";
    }

    @Execute(name = "nick", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id", failureCode = 23),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "nick", failureCode = 26),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = MemberService.VALIDATOR_EXISTS, parameter = "id", failureCode = 24),
            @Validate(validator = MemberService.VALIDATOR_SELF_MANAGER, parameter = "id", failureCode = 27)
    })
    public Object nick() {
        memberService.nick(request.get("id"), request.get("nick"));

        return "";
    }

    @Execute(name = "leave", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id", failureCode = 23),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = MemberService.VALIDATOR_EXISTS, parameter = "id", failureCode = 24),
            @Validate(validator = MemberService.VALIDATOR_SELF_MANAGER, parameter = "id", failureCode = 27)
    })
    public Object leave() {
        memberService.leave(request.get("id"));

        return "";
    }
}
