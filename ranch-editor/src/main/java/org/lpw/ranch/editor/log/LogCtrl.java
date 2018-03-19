package org.lpw.ranch.editor.log;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(LogModel.NAME + ".ctrl")
@Execute(name = "/editor/log/", key = LogModel.NAME, code = "0")
public class LogCtrl {
    @Inject
    private Request request;
    @Inject
    private LogService logService;
}
