package org.lpw.ranch.chrome;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ChromeService.VALIDATOR_KEY_EXISTS)
public class KeyExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private ChromeService chromeService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return chromeService.findByKey(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return ChromeModel.NAME + ".key.not-exists";
    }
}
