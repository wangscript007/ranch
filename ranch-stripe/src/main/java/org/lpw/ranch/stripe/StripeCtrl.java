package org.lpw.ranch.stripe;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(StripeModel.NAME + ".ctrl")
@Execute(name = "/stripe/", key = StripeModel.NAME, code = "44")
public class StripeCtrl {
    @Inject
    private Request request;
    @Inject
    private StripeService stripeService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN, string = {"ranch-stripe"})
    })
    public Object query() {
        return stripeService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "publishable", failureCode = 4),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "secret", failureCode = 6),
            @Validate(validator = Validators.SIGN, string = {"ranch-stripe"})
    })
    public Object save() {
        stripeService.save(request.setToModel(StripeModel.class));

        return "";
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 8),
            @Validate(validator = Validators.SIGN, string = {"ranch-stripe"})
    })
    public Object delete() {
        stripeService.delete(request.get("id"));

        return "";
    }
}
