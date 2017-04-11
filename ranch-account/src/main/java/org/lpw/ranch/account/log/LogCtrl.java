package org.lpw.ranch.account.log;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(LogModel.NAME + ".ctrl")
@Execute(name = "/account/log/", key = LogModel.NAME, code = "22")
public class LogCtrl {
    @Inject
    private Request request;
    @Inject
    private LogService logService;
}
