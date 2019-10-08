package org.lpw.ranch.paypal;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PaypalModel.NAME + ".ctrl")
@Execute(name = "/paypal/", key = PaypalModel.NAME, code = "142")
public class PaypalCtrl {
    @Inject
    private Request request;
    @Inject
    private PaypalService paypalService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN, string = {"ranch-paypal"})
    })
    public Object query() {
        return paypalService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "appId", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appId", failureCode = 5),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "secret", failureCode = 6),
            @Validate(validator = Validators.SIGN, string = {"ranch-paypal"}),
            @Validate(validator = PaypalService.VALIDATOR_NOT_EXISTS, parameters = {"key", "appId"}, failureCode = 7)
    })
    public Object save() {
        return paypalService.save(request.setToModel(PaypalModel.class));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 8),
            @Validate(validator = Validators.SIGN, string = {"ranch-paypal"})
    })
    public Object delete() {
        paypalService.delete(request.get("id"));

        return "";
    }
}
