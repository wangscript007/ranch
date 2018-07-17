package org.lpw.ranch.user;

import org.lpw.ranch.user.auth.AuthService;
import org.lpw.ranch.user.type.Types;
import org.lpw.ranch.weixin.helper.WeixinHelper;
import org.lpw.tephra.ctrl.Forward;
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
    private Forward forward;
    @Inject
    private UserService userService;

    @Execute(name = "introducer")
    public Object introducer() {
        return userService.introducer(request.get("code"));
    }

    @Execute(name = "sign-up", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "uid", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "uid", failureCode = 2),
            @Validate(validator = UserService.VALIDATOR_PASSWORD, parameters = {"password", "type"}, failureCode = 3),
            @Validate(validator = Validators.BETWEEN, number = {0, Types.MAX}, parameter = "type", failureCode = 27),
            @Validate(validator = AuthService.VALIDATOR_UID_NOT_EXISTS, parameters = {"uid", "password", "type"}, failureCode = 4)
    })
    public Object signUp() {
        userService.signUp(request.get("uid"), request.get("password"), request.getAsInt("type"));

        return sign();
    }

    @Execute(name = "sign-in", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "uid", failureCode = 1),
            @Validate(validator = UserService.VALIDATOR_PASSWORD, parameters = {"password", "type"}, failureCode = 3),
            @Validate(validator = Validators.BETWEEN, number = {0, Types.MAX}, parameter = "type", failureCode = 27),
            @Validate(validator = UserService.VALIDATOR_SIGN_IN, parameters = {"uid", "password", "type"}, failureCode = 6)
    })
    public Object signIn() {
        return sign();
    }

    @Execute(name = "sign-in-wx-pc", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "redirectUrl", failureCode = 29),
            @Validate(validator = WeixinHelper.VALIDATOR_EXISTS, parameter = "key", failureCode = 30)
    })
    public Object signInWxPc() {
        forward.redirectTo(userService.signInWxPc(request.get("key"), request.get("redirectUrl")));

        return null;
    }

    @Execute(name = "sign-in-wx-pc-redirect")
    public Object signInWxPcRedirect() {
        forward.redirectTo(userService.signInWxPcRedirect(request.get("code")));

        return null;
    }

    @Execute(name = "sign")
    public Object sign() {
        return templates.get().success(userService.sign(), null);
    }

    @Execute(name = "sign-out")
    public Object signOut() {
        userService.signOut();

        return "";
    }

    @Execute(name = "modify", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "idcard", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 8),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "nick", failureCode = 9),
            @Validate(validator = Validators.MOBILE, emptyable = true, parameter = "mobile", failureCode = 10),
            @Validate(validator = Validators.EMAIL, emptyable = true, parameter = "email", failureCode = 11),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "email", failureCode = 12),
            @Validate(validator = Validators.BETWEEN, number = {0, 2}, parameter = "gender", failureCode = 13),
            @Validate(validator = UserService.VALIDATOR_SIGN)
    })
    public Object modify() {
        userService.modify(request.setToModel(UserModel.class));

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
                templates.get().failure(1517, message.get(UserModel.NAME + ".password.illegal"), null, null);
    }

    @Execute(name = "get", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 21),
            @Validate(validator = Validators.SIGN)
    })
    public Object get() {
        return userService.get(request.getAsArray("ids"));
    }

    @Execute(name = "find-by-code", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "code", failureCode = 26),
            @Validate(validator = Validators.SIGN)
    })
    public Object find() {
        return templates.get().success(userService.findByCode(request.get("code")), null);
    }

    @Execute(name = "find-by-uid", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "uid", failureCode = 27),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = AuthService.VALIDATOR_UID_EXISTS, parameter = "uid", failureCode = 28)
    })
    public Object findByUid() {
        return templates.get().success(userService.findByUid(request.get("uid")), null);
    }

    @Execute(name = "find-sign", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "idUidCode", failureCode = 31),
            @Validate(validator = Validators.SIGN)
    })
    public Object findOrSign() {
        return templates.get().success(userService.findOrSign(request.get("idUidCode")), null);
    }

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.MOBILE, emptyable = true, parameter = "mobile", failureCode = 10),
            @Validate(validator = Validators.EMAIL, emptyable = true, parameter = "email", failureCode = 11),
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return userService.query(request.get("uid"), request.get("idcard"), request.get("name"), request.get("nick"),
                request.get("mobile"), request.get("email"), request.get("code"), request.getAsInt("minGrade", -1),
                request.getAsInt("maxGrade", -1), request.getAsInt("state", -1),
                request.get("registerStart"), request.get("registerEnd"));
    }

    @Execute(name = "grade", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 22),
            @Validate(validator = Validators.BETWEEN, number = {0, 99}, parameter = "grade", failureCode = 23),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = UserService.VALIDATOR_EXISTS, parameter = "id", failureCode = 25)
    })
    public Object grade() {
        userService.grade(request.get("id"), request.getAsInt("grade"));

        return "";
    }

    @Execute(name = "state", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 22),
            @Validate(validator = Validators.BETWEEN, number = {0, 1}, parameter = "state", failureCode = 24),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = UserService.VALIDATOR_EXISTS, parameter = "id", failureCode = 25)
    })
    public Object state() {
        userService.state(request.get("id"), request.getAsInt("state"));

        return "";
    }
}
