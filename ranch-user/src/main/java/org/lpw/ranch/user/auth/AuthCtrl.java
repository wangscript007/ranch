package org.lpw.ranch.user.auth;

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
}
