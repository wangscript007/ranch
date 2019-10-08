package org.lpw.ranch.access;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AccessModel.NAME + ".ctrl")
@Execute(name = "/access/", key = AccessModel.NAME, code = "134")
public class AccessCtrl {
    @Inject
    private Request request;
    @Inject
    private AccessService accessService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return accessService.query(request.get("host"), request.get("uri"), request.get("user"),
                request.get("userAgent"), request.get("start"), request.get("end"));
    }
}
