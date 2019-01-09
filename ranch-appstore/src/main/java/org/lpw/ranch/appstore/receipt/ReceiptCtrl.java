package org.lpw.ranch.appstore.receipt;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ReceiptModel.NAME + ".ctrl")
@Execute(name = "/appstore/receipt/", key = ReceiptModel.NAME, code = "38")
public class ReceiptCtrl {
    @Inject
    private Request request;
    @Inject
    private ReceiptService receiptService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return receiptService.query(request.get("user"), request.getAsArray("time"));
    }

    @Execute(name = "verify", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "data", failureCode = 21),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object verify() {
        return receiptService.verify(request.get("data"));
    }
}
