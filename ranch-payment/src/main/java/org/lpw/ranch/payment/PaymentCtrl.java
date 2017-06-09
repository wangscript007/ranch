package org.lpw.ranch.payment;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PaymentModel.NAME + ".ctrl")
@Execute(name = "/payment/", key = PaymentModel.NAME, code = "25")
public class PaymentCtrl {
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private Request request;
    @Inject
    private PaymentService paymentService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        String state = request.get("state");
        if (validator.isEmpty(state))
            state = "-1";

        return paymentService.query(request.get("type"), request.get("user"), request.get("orderNo"),
                request.get("tradeNo"), converter.toInt(state), request.get("start"), request.get("end"));
    }

    @Execute(name = "success", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 12),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = PaymentService.VALIDATOR_EXISTS, parameter = "id", failureCode = 11)
    })
    public Object success() {
        return paymentService.success(request.get("id"), request.getMap());
    }

    @Execute(name = "notice", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 12),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = PaymentService.VALIDATOR_EXISTS, parameter = "id", failureCode = 11)
    })
    public Object notice() {
        return paymentService.notice(request.get("id"));
    }

    @Execute(name = "create", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "user", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "user", failureCode = 4),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "notify", failureCode = 6)
    })
    public Object create() {
        return paymentService.create(request.get("type"), request.get("user"), request.getAsInt("amount"), request.get("notify"), request.getMap());
    }

    @Execute(name = "complete", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "orderNo", failureCode = 7),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 5),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "tradeNo", failureCode = 8),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "tradeNo", failureCode = 9),
            @Validate(validator = Validators.BETWEEN, number = {1, 2}, parameter = "state", failureCode = 10),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = PaymentService.VALIDATOR_EXISTS, parameter = "orderNo", failureCode = 11)
    })
    public Object complete() {
        return paymentService.complete(request.get("orderNo"), request.getAsInt("amount"), request.get("tradeNo"), request.getAsInt("state"), request.getMap());
    }
}
