package org.lpw.ranch.push.log;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
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
}
