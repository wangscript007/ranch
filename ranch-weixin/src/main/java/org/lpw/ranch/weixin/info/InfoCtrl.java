package org.lpw.ranch.weixin.info;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(InfoModel.NAME + ".ctrl")
@Execute(name = "/weixin/info/", key = InfoModel.NAME, code = "24")
public class InfoCtrl {
    @Inject
    private Request request;
    @Inject
    private InfoService infoService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN, string = {"ranch-weixin"})
    })
    public Object query() {
        return infoService.query();
    }
}
