package org.lpw.ranch.group.member;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MemberService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private MemberService memberService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return memberService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return MemberModel.NAME + ".not-exists";
    }
}
