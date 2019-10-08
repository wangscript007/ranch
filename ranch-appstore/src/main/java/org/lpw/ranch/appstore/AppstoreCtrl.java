package org.lpw.ranch.appstore;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AppstoreModel.NAME + ".ctrl")
@Execute(name = "/appstore/", key = AppstoreModel.NAME, code = "138")
public class AppstoreCtrl {
    @Inject
    private Request request;
    @Inject
    private AppstoreService appstoreService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return appstoreService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "productId", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "productId", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 4),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        appstoreService.save(request.get("productId"), request.get("name"), request.getAsInt("amount"));

        return "";
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 11),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        appstoreService.delete(request.get("id"));

        return "";
    }
}
