package org.lpw.ranch.ad;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AdService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private AdDao adDao;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return adDao.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return AdModel.NAME + ".not-exists";
    }
}
