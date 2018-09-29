package org.lpw.ranch.address;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AddressService.VALIDATOR_OWNER)
public class OwnerValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private AddressService addressService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        AddressModel address = addressService.findById(parameter);

        return address != null && address.getUser().equals(userHelper.id());
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return AddressModel.NAME + ".not-owner";
    }
}
