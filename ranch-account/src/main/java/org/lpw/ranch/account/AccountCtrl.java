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
            @Validate(validator = UserHelper.VALIDATOR_NOT_EMPTY_OR_SIGN_IN, parameter = "user", failureCode = 5)
    })
    public Object query() {
        return accountService.query(request.get("user"), request.get("owner"));
    }

    @Execute(name = "deposit", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {36}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 9}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "channel", failureCode = 3),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 4),
            @Validate(validator = UserHelper.VALIDATOR_NOT_EMPTY_OR_SIGN_IN, parameter = "user", failureCode = 5)
    })
    public Object deposit() {
        return execute(accountService.deposit(request.get("user"), request.get("owner"), request.getAsInt("type"), request.get("channel"), request.getAsInt("amount"), request.getMap()), 6, "deposit");
    }

    @Execute(name = "withdraw", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {36}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 9}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "channel", failureCode = 3),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 4),
            @Validate(validator = UserHelper.VALIDATOR_NOT_EMPTY_OR_SIGN_IN, parameter = "user", failureCode = 5)
    })
    public Object withdraw() {
        return execute(accountService.withdraw(request.get("user"), request.get("owner"), request.getAsInt("type"), request.get("channel"), request.getAsInt("amount"), request.getMap()), 7, "withdraw");
    }

    @Execute(name = "reward", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {36}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 9}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "channel", failureCode = 3),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 4),
            @Validate(validator = UserHelper.VALIDATOR_NOT_EMPTY_OR_SIGN_IN, parameter = "user", failureCode = 5)
    })
    public Object reward() {
        return execute(accountService.reward(request.get("user"), request.get("owner"), request.getAsInt("type"), request.get("channel"), request.getAsInt("amount")), 8, "reward");
    }

    @Execute(name = "profit", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {36}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 9}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "channel", failureCode = 3),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 4),
            @Validate(validator = UserHelper.VALIDATOR_NOT_EMPTY_OR_SIGN_IN, parameter = "user", failureCode = 5)
    })
    public Object profit() {
        return execute(accountService.profit(request.get("user"), request.get("owner"), request.getAsInt("type"), request.get("channel"), request.getAsInt("amount")), 9, "profit");
    }

    @Execute(name = "consume", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {36}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 9}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "channel", failureCode = 3),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 4),
            @Validate(validator = UserHelper.VALIDATOR_NOT_EMPTY_OR_SIGN_IN, parameter = "user", failureCode = 5)
    })
    public Object consume() {
        return execute(accountService.consume(request.get("user"), request.get("owner"), request.getAsInt("type"), request.get("channel"), request.getAsInt("amount")), 10, "consume");
    }

    @Execute(name = "remit-in", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {36}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 9}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "channel", failureCode = 3),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 4),
            @Validate(validator = UserHelper.VALIDATOR_NOT_EMPTY_OR_SIGN_IN, parameter = "user", failureCode = 5)
    })
    public Object remitIn() {
        return execute(accountService.remitIn(request.get("user"), request.get("owner"), request.getAsInt("type"), request.get("channel"), request.getAsInt("amount")), 11, "remit-in");
    }

    @Execute(name = "remit-out", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {36}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 9}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "channel", failureCode = 3),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 4),
            @Validate(validator = UserHelper.VALIDATOR_NOT_EMPTY_OR_SIGN_IN, parameter = "user", failureCode = 5)
    })
    public Object remitOut() {
        return execute(accountService.remitOut(request.get("user"), request.get("owner"), request.getAsInt("type"), request.get("channel"), request.getAsInt("amount")), 12, "remit-out");
    }

    private Object execute(JSONObject object, int code, String type) {
        return object == null ? templates.get().failure(2200 + code, message.get(AccountModel.NAME + "." + type + ".failure"), null, null) : object;
    }
}
