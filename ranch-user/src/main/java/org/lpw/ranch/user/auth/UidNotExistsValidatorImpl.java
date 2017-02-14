package org.lpw.ranch.user.auth;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AuthService.VALIDATOR_UID_NOT_EXISTS)
public class UidNotExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private AuthService authService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return authService.findByUid(parameter) == null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return AuthModel.NAME + ".uid.exists";
    }
}
