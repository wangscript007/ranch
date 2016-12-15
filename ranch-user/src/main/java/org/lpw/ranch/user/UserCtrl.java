package org.lpw.ranch.user;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(UserModel.NAME + ".ctrl")
@Execute(name = "/user/", key = UserModel.NAME, code = "15")
public class UserCtrl {
    @Autowired
    protected Request request;
    @Autowired
    protected UserService userService;
}
