package org.lpw.ranch.account;

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
@Controller(AccountModel.NAME + ".ctrl")
@Execute(name = "/account/", key = AccountModel.NAME, code = "22")
public class AccountCtrl {
    @Inject
    private Request request;
    @Inject
    private AccountService accountService;

    @Execute(name = "deposit", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 0}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 3),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS, parameter = "user", failureCode = 4)
    })
    public Object deposit() {
        return accountService.deposit(request.get("user"), request.get("owner"), request.getAsInt("type"), request.getAsInt("amount"));
    }

    @Execute(name = "withdraw", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "owner", failureCode = 1),
            @Validate(validator = Validators.BETWEEN, number = {0, 0}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 3),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object withdraw() {
        return accountService.withdraw(request.get("owner"), request.getAsInt("type"), request.getAsInt("amount"));
    }
}
