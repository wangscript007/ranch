package org.lpw.ranch.account.log;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(LogModel.NAME + ".ctrl")
@Execute(name = "/account/log/", key = LogModel.NAME, code = "22")
public class LogCtrl {
    @Inject
    private Validator validator;
    @Inject
    private Request request;
    @Inject
    private LogService logService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return logService.query(request.get("uid"), request.get("type"), validator.isEmpty(request.get("state")) ? -1 : request.getAsInt("state"), request.getAsSqlDate("start"), request.getAsSqlDate("end"));
    }

    @Execute(name = "pass", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 21),
            @Validate(validator = Validators.SIGN)
    })
    public Object pass() {
        logService.pass(request.getAsArray("ids"));

        return "";
    }

    @Execute(name = "reject", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 21),
            @Validate(validator = Validators.SIGN)
    })
    public Object reject() {
        logService.reject(request.getAsArray("ids"));

        return "";
    }
}
