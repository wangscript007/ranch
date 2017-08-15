package org.lpw.ranch.user.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller("ranch.user.helper.ctrl")
@Execute(name = "/user/",key = "ranch.user.helper", code = "10")
public class TestUserCtrl {
    @Execute(name = "sign", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object sign() {
        JSONObject object = new JSONObject();
        object.put("state", "sign in");

        return object;
    }

    @Execute(name = "exists-or-sign-in", validates = {
            @Validate(validator = UserHelper.VALIDATOR_EXISTS_OR_SIGN_IN, parameter = "id", failureCode = 91)
    })
    public Object existsOrSignIn() {
        JSONObject object = new JSONObject();
        object.put("state", "exists or sign in");

        return object;
    }

    @Execute(name = "get", validates = {
            @Validate(validator = UserHelper.VALIDATOR_EXISTS, parameter = "id")
    })
    public Object get() {
        JSONObject object = new JSONObject();
        object.put("state", "exists");

        return object;
    }
}
