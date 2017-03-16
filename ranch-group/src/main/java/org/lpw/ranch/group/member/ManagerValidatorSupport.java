package org.lpw.ranch.group.member;

import com.alibaba.fastjson.JSONObject;
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
        JSONObject manager = memberService.find(group, user);

        return !manager.isEmpty() && manager.getIntValue("type") >= MemberService.Type.Manager.ordinal();
    }
}
