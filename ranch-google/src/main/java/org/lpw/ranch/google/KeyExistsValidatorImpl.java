package org.lpw.ranch.google;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(GoogleService.VALIDATOR_KEY_EXISTS)
public class KeyExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private GoogleService googleService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return googleService.findByKey(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return GoogleModel.NAME + ".key.not-exits";
    }
}
