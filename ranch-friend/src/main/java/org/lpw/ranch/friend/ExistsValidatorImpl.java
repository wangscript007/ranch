package org.lpw.ranch.friend;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(FriendService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private FriendService friendService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return friendService.find(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return FriendModel.NAME + ".not-exists";
    }
}
