package org.lpw.ranch.popular;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PopularModel.NAME + ".ctrl")
@Execute(name = "/popular/", key = PopularModel.NAME, code = "43")
public class PopularCtrl {
    @Inject
    private Request request;
    @Inject
    private PopularService popularService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return popularService.query(request.get("key"));
    }

    @Execute(name = "publish", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2)
    })
    public Object publish() {
        return popularService.publish(request.get("key"), request.getAsInt("size", 10));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        popularService.delete(request.get("id"));

        return "";
    }
}
