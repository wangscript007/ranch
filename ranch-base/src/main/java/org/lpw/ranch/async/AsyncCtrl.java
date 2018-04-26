package org.lpw.ranch.async;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AsyncModel.NAME + ".ctrl")
@Execute(name = "/async/")
public class AsyncCtrl {
    @Inject
    private Request request;
    @Inject
    private AsyncService asyncService;

    @Execute(name = "find")
    public Object find() {
        return asyncService.find(request.get("id"));
    }
}
