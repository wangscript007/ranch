package org.lpw.ranch.push.ios;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(IosModel.NAME + ".ctrl")
@Execute(name = "/push/ios/", key = IosModel.NAME, code = "0")
public class IosCtrl {
    @Inject
    private Request request;
    @Inject
    private IosService iosService;
}
