package org.lpw.ranch.group.member;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MemberService.VALIDATOR_USER_EXISTS)
public class UserExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private MemberService memberService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        JSONObject object = memberService.find(parameters[0], parameters[1]);

        return !object.isEmpty() && object.getIntValue("type") >= MemberService.Type.Normal.ordinal();
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return MemberModel.NAME + ".user.not-exists";
    }
}
