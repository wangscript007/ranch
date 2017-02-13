package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller("ranch.base.user.ctrl")
@Execute(name = "/base/user/", code = "10")
public class TestUserCtrl {
    @Execute(name = "sign", validates = {
            @Validate(validator = User.VALIDATOR_SIGN_IN)
    })
    public Object sign() {
        JSONObject object = new JSONObject();
        object.put("state", "sign in");

        return object;
    }
}
