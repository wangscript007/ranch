package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Message;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AccountModel.NAME + ".ctrl")
@Execute(name = "/account/", key = AccountModel.NAME, code = "22")
public class AccountCtrl {
    @Inject
    private Message message;
    @Inject
    private Request request;
    @Inject
    private Templates templates;
    @Inject
    private AccountService accountService;

    @Execute(name = "query", validates = {
            @Validate(validator = UserHelper.VALIDATOR_ID_OR_SIGN_IN, parameter = "user", failureCode = 10)
    })
    public Object query() {
        return accountService.query(request.get("user"), request.get("owner"));
    }

    @Execute(name = "deposit", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 0}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 3),
            @Validate(validator = UserHelper.VALIDATOR_ID_OR_SIGN_IN, parameter = "user", failureCode = 10)
    })
    public Object deposit() {
        return execute(accountService.deposit(request.get("user"), request.get("owner"), request.getAsInt("type"), request.getAsInt("amount"), request.getMap()), 5, "deposit");
    }

    @Execute(name = "withdraw", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 0}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 3),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object withdraw() {
        return execute(accountService.withdraw(request.get("owner"), request.getAsInt("type"), request.getAsInt("amount"), request.getMap()), 6, "withdraw");
    }

    @Execute(name = "reward", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 0}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 3),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS, parameter = "user", failureCode = 4)
    })
    public Object reward() {
        return execute(accountService.reward(request.get("user"), request.get("owner"), request.getAsInt("type"), request.getAsInt("amount")), 7, "reward");
    }

    @Execute(name = "profit", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 0}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 3),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS, parameter = "user", failureCode = 4)
    })
    public Object profit() {
        return execute(accountService.profit(request.get("user"), request.get("owner"), request.getAsInt("type"), request.getAsInt("amount")), 8, "profit");
    }

    @Execute(name = "consume", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 0}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 3),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object consume() {
        return execute(accountService.consume(request.get("owner"), request.getAsInt("type"), request.getAsInt("amount")), 9, "consume");
    }

    private Object execute(JSONObject object, int code, String type) {
        return object == null ? templates.get().failure(2200 + code, message.get(AccountModel.NAME + "." + type + ".failure"), null, null) : object;
    }
}
