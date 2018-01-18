package org.lpw.ranch.device;

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
@Controller(DeviceModel.NAME + ".ctrl")
@Execute(name = "/device/", key = DeviceModel.NAME, code = "32")
public class DeviceCtrl {
    @Inject
    private Request request;
    @Inject
    private DeviceService deviceService;

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "appCode", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appCode", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 4),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "macId", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "macId", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "version", failureCode = 7),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS_OR_SIGN_IN, parameter = "user", failureCode = 8)
    })
    public Object save() {
        return deviceService.save(request.get("user"), request.get("appCode"), request.get("type"), request.get("macId"), request.get("version"));
    }
}
