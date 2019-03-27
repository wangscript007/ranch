package org.lpw.ranch.editor.price;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PriceService.VALIDATOR_EXISTS)
public class ExistsValidatoImpl extends ValidatorSupport {
    @Inject
    private PriceDao priceDao;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return priceDao.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return PriceModel.NAME + ".not-exists";
    }
}
