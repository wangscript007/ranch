package org.lpw.ranch.form.user;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserModel.NAME + ".ctrl")
@Execute(name = "/form/user/", key = UserModel.NAME, code = "0")
public class UserCtrl {
    @Inject
    private Request request;
    @Inject
    private UserService userService;
}
