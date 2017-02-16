package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller("ranch.user-helper.ctrl")
@Execute(name = "/user/", code = "10")
public class TestUserCtrl {
    @Execute(name = "sign", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object sign() {
        JSONObject object = new JSONObject();
        object.put("state", "sign in");

        return object;
    }
}
