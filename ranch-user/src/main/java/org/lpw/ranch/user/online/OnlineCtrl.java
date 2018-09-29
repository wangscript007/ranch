package org.lpw.ranch.user.online;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(OnlineModel.NAME + ".ctrl")
@Execute(name = "/user/online/", key = OnlineModel.NAME, code = "15")
public class OnlineCtrl {
    @Inject
    private Request request;
    @Inject
    private OnlineService onlineService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return onlineService.query(request.get("user"), request.get("uid"), request.get("ip"));
    }

    @Execute(name = "sign-out", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object signOut() {
        onlineService.signOutId(request.get("id"));

        return "";
    }
}
