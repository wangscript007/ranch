package org.lpw.ranch.push.log;

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
@Execute(name = "/push/log/", key = LogModel.NAME, code = "31")
public class LogCtrl {
    @Inject
    private Request request;
    @Inject
    private LogService logService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return logService.query(request.get("user"), request.get("reciever"), request.get("appCode"), request.get("sender"),
                request.getAsInt("state"), request.get("start"), request.get("end"));
    }

    @Execute(name = "uquery")
    public Object uquery() {
        return logService.query(request.get("receiver"), request.get("appCode"));
    }

    @Execute(name = "unread")
    public Object unread() {
        return logService.unread(request.get("receiver"), request.get("appCode"));
    }

    @Execute(name = "read")
    public Object read() {
        logService.read(request.get("id"));

        return "";
    }

    @Execute(name = "reads")
    public Object reads() {
        logService.reads(request.get("receiver"), request.get("appCode"));

        return "";
    }
}
