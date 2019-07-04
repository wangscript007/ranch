package org.lpw.ranch.ad;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AdModel.NAME + ".ctrl")
@Execute(name = "/ad/", key = AdModel.NAME, code = "46")
public class AdCtrl {
    @Inject
    private Request request;
    @Inject
    private AdService adService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return adService.query(request.get("type"), request.getAsInt("state", -1));
    }

    @Execute(name = "publish")
    public Object publish() {
        return adService.publish(request.get("type"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "resource", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "resouce", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "operation", failureCode = 6),
            @Validate(validator = Validators.IN, number = {0, 1}, parameter = "state", failureCode = 7),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        adService.save(request.setToModel(AdModel.class));

        return "";
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 8),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        adService.delete(request.get("id"));

        return "";
    }
}
