package org.lpw.ranch.editor.role;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoleModel.NAME + ".ctrl")
@Execute(name = "/editor/role/", key = RoleModel.NAME, code = "32")
public class RoleCtrl {
    @Inject
    private Request request;
    @Inject
    private RoleService roleService;

    @Execute(name = "delete", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 42),
            @Validate(validator = RoleService.VALIDATOR_DELETABLE, parameters = {"user", "editor"}, failureCode = 43),
    })
    public Object delete() {
        roleService.delete(request.get("user"), request.get("editor"));

        return "";
    }
}
