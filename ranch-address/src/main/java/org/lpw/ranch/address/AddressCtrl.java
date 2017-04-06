package org.lpw.ranch.address;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AddressModel.NAME + ".ctrl")
@Execute(name = "/address/", key = AddressModel.NAME, code = "21")
public class AddressCtrl {
    @Inject
    private Request request;
    @Inject
    private AddressService addressService;

    @Execute(name = "query", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object query() {
        return addressService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.ID, parameter = "region", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "detail", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "detail", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "postcode", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "latitude", failureCode = 6),
            @Validate(validator = Validators.LATITUDE, parameter = "latitude", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "longitude", failureCode = 8),
            @Validate(validator = Validators.LONGITUDE, parameter = "longitude", failureCode = 9),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "label", failureCode = 10),
            @Validate(validator = Validators.BETWEEN, number = {0, 1}, parameter = "major", failureCode = 11),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = AddressService.VALIDATOR_UPDATABLE, emptyable = true, parameter = "id", failureCode = 1)
    })
    public Object save() {
        return addressService.save(request.setToModel(new AddressModel()));
    }

    @Execute(name = "use", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = AddressService.VALIDATOR_UPDATABLE, parameter = "id", failureCode = 1)
    })
    public Object use() {
        return addressService.use(request.get("id"));
    }

    @Execute(name = "major", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = AddressService.VALIDATOR_UPDATABLE, parameter = "id", failureCode = 1)
    })
    public Object major() {
        return addressService.major(request.get("id"));
    }
}
