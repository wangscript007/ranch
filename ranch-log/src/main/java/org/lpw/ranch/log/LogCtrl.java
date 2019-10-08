package org.lpw.ranch.log;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(LogModel.NAME + ".ctrl")
@Execute(name = "/log/", key = LogModel.NAME, code = "141")
public class LogCtrl {
    @Inject
    private Request request;
    @Inject
    private LogService logService;

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 2)
    })
    public Object save() {
        logService.save(request.get("type"));

        return "";
    }
}
