package org.lpw.ranch.editor.buy;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(BuyService.VALIDATOR_NOT_EXISTS)
public class NotExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private BuyService buyService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return buyService.find(userHelper.id(), parameter) == null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return BuyModel.NAME + ".exists";
    }
}
