package org.lpw.ranch.user.auth;

import org.lpw.ranch.user.UserService;
import org.lpw.ranch.user.type.Types;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AuthModel.NAME + ".ctrl")
@Execute(name = "/user/auth/", key = AuthModel.NAME, code = "15")
public class AuthCtrl {
    @Inject
    private Request request;
    @Inject
    private AuthService authService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "user", failureCode = 51),
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return authService.query(request.get("user"));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "uid", failureCode = 1),
            @Validate(validator = UserService.VALIDATOR_PASSWORD, parameters = {"password", "type"}, failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "macId", failureCode = 5),
            @Validate(validator = Validators.BETWEEN, number = {0, Types.MAX}, parameter = "type", failureCode = 27),
            @Validate(validator = UserService.VALIDATOR_SIGN_IN, parameters = {"uid", "password", "macId", "type"}, failureCode = 6)
    })
    public Object delete() {
        authService.delete(request.get("uid"));

        return "";
    }
}
