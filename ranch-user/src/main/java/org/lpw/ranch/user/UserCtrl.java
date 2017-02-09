package org.lpw.ranch.user;

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
@Controller(UserModel.NAME + ".ctrl")
@Execute(name = "/user/", key = UserModel.NAME, code = "15")
public class UserCtrl {
    @Inject
    private Message message;
    @Inject
    private Request request;
    @Inject
    private Templates templates;
    @Inject
    private UserService userService;

    @Execute(name = "sign-up", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "uid", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "uid", failureCode = 2),
            @Validate(validator = UserService.VALIDATOR_PASSWORD, parameter = "password", failureCode = 3),
            @Validate(validator = UserService.VALIDATOR_SIGN_UP, parameters = {"uid", "password", "type"}, failureCode = 4)
    })
    public Object signUp() {
        return sign();
    }

    @Execute(name = "sign-in", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "uid", failureCode = 1),
            @Validate(validator = UserService.VALIDATOR_PASSWORD, parameter = "password", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "macId", failureCode = 5),
            @Validate(validator = UserService.VALIDATOR_SIGN_IN, parameters = {"uid", "password", "macId", "type"}, failureCode = 6)
    })
    public Object signIn() {
        return sign();
    }

    @Execute(name = "sign")
    public Object sign() {
        return templates.get().success(userService.sign(), null);
    }

    @Execute(name = "modify", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, emptyable = true, parameter = "name", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, emptyable = true, parameter = "nick", failureCode = 8),
            @Validate(validator = Validators.MOBILE, emptyable = true, parameter = "mobile", failureCode = 9),
            @Validate(validator = Validators.EMAIL, emptyable = true, parameter = "email", failureCode = 10),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, emptyable = true, parameter = "email", failureCode = 11),
            @Validate(validator = Validators.BETWEEN, number = {0, 2}, emptyable = true, parameter = "gender", failureCode = 12),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, emptyable = true, parameter = "address", failureCode = 13),
            @Validate(validator = UserService.VALIDATOR_SIGN)
    })
    public Object modify() {
        userService.modify(request.setToModel(new UserModel()));

        return sign();
    }

    @Execute(name = "password", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "new", failureCode = 14, failureArgKeys = {UserModel.NAME + ".password.new"}),
            @Validate(validator = Validators.NOT_EQUALS, parameters = {"old", "new"}, failureCode = 15, failureArgKeys = {UserModel.NAME + ".password.new", UserModel.NAME + ".password.old"}),
            @Validate(validator = Validators.EQUALS, parameters = {"new", "repeat"}, failureCode = 16, failureArgKeys = {UserModel.NAME + ".password.repeat", UserModel.NAME + ".password.new"}),
            @Validate(validator = UserService.VALIDATOR_SIGN)
    })
    public Object password() {
        return userService.password(request.get("old"), request.get("new")) ? "" :
                templates.get().failure(17, message.get(UserModel.NAME + ".password.illegal"), null, null);
    }

    @Execute(name = "get", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 21),
            @Validate(validator = Validators.SIGN)
    })
    public Object get() {
        return userService.get(request.getAsArray("ids"));
    }
}
