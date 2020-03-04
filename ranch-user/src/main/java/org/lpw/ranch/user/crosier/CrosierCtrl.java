package org.lpw.ranch.user.crosier;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(CrosierModel.NAME + ".ctrl")
@Execute(name = "/user/crosier/", key = CrosierModel.NAME, code = "115")
public class CrosierCtrl {
    @Inject
    private Request request;
    @Inject
    private CrosierService crosierService;

    @Execute(name = "grades", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object grades() {
        return crosierService.grades();
    }

    @Execute(name = "pathes", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object pathes() {
        return crosierService.pathes(request.getAsInt("grade"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        crosierService.save(request.getAsInt("grade"), request.get("pathes"));

        return "";
    }
}
