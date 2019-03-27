package org.lpw.ranch.stripe.transaction;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.stripe.StripeService;
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
@Execute(name = "/stripe/transaction/", key = TransactionModel.NAME, code = "44")
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

    @Execute(name = "charge", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 21),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "tradeNo", failureCode = 22),
            @Validate(validator = StripeService.VALIDATOR_KEY_EXISTS, parameter = "key", failureCode = 23),
            @Validate(validator = TransactionService.VALIDATOR_TRADE_NO_NOT_EXISTS, parameter = "tradeNo", failureCode = 24)
    })
    public Object charge() {
        JSONObject object = transactionService.charge(request.get("key"), request.getAsInt("amount"), request.get("currency"),
                request.get("tradeNo"));

        return object == null ? templates.get().failure(4425, message.get(TransactionModel.NAME + ".charge.failure"),
                null, null) : object;
    }
}
