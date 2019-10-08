package org.lpw.ranch.appstore.transaction;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(TransactionModel.NAME + ".ctrl")
@Execute(name = "/appstore/transaction/", key = TransactionModel.NAME, code = "138")
public class TransactionCtrl {
    @Inject
    private Request request;
    @Inject
    private TransactionService transactionService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return transactionService.query(request.get("user"), request.get("transactionId"), request.get("productId"),
                request.getAsArray("time"));
    }
}
