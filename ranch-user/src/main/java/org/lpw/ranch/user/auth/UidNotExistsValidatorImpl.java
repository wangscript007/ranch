package org.lpw.ranch.user.auth;

import org.lpw.ranch.user.type.Types;
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
    private Types types;
    @Inject
    private AuthService authService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        String uid = types.getUid(parameters[0], parameters[1], numeric.toInt(parameters[2]));

        return uid != null && authService.findByUid(uid) == null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return AuthModel.NAME + ".uid.exists";
    }
}
