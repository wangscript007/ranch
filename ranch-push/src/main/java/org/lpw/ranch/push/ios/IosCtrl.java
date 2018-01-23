package org.lpw.ranch.push.ios;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(IosModel.NAME + ".ctrl")
@Execute(name = "/push/ios/", key = IosModel.NAME, code = "31")
public class IosCtrl {
    @Inject
    private Request request;
    @Inject
    private IosService iosService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return iosService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "appCode", failureCode = 21),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appCode", failureCode = 22),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "p12", failureCode = 23),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "password", failureCode = 24),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "password", failureCode = 25),
            @Validate(validator = Validators.BETWEEN, number = {0, 1}, parameter = "destination", failureCode = 26),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        return iosService.save(request.get("appCode"), request.get("p12"), request.get("password"), request.getAsInt("destination"));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 27),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        iosService.delete(request.get("id"));

        return "";
    }
}
