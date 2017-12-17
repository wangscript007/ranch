package org.lpw.ranch.logger;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(LoggerModel.NAME + ".ctrl")
@Execute(name = "/logger/")
public class LoggerCtrl {
    @Inject
    private Request request;
    @Inject
    private LoggerService loggerService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return loggerService.query(request.get("key"), request.getAsInt("state", -1), request.get("start"), request.get("end"));
    }

    @Execute(name = "state", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object state() {
        loggerService.state(request.get("id"), request.getAsInt("state"));

        return "";
    }
}
