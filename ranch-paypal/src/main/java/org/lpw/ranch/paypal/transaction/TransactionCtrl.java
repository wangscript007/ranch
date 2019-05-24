package org.lpw.ranch.paypal.transaction;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.paypal.PaypalService;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Message;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(TransactionModel.NAME + ".ctrl")
@Execute(name = "/paypal/transaction/", key = TransactionModel.NAME, code = "42")
public class TransactionCtrl {
    @Inject
    private Json json;
    @Inject
    private Message message;
    @Inject
    private Request request;
    @Inject
    private Templates templates;
    @Inject
    private TransactionService transactionService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return transactionService.query(request.get("user"), request.getAsArray("create"));
    }

    @Execute(name = "verify", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 21),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "tradeNo", failureCode = 22),
            @Validate(validator = PaypalService.VALIDATOR_EXISTS, parameter = "key", failureCode = 23)
    })
    public Object verify() {
        JSONObject object = json.toObject(transactionService.verify(request.get("key"), request.get("tradeNo")));

        return object == null ? templates.get().failure(4224, message.get(TransactionModel.NAME + ".verify.illegal"),
                null, null) : object;
    }
}
