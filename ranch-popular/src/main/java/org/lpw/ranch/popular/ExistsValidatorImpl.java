package org.lpw.ranch.popular;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PopularService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private PopularDao popularDao;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return popularDao.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return PopularModel.NAME + ".not-exists";
    }
}
