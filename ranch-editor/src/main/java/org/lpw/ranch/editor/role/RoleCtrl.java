package org.lpw.ranch.editor.role;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoleModel.NAME + ".ctrl")
@Execute(name = "/editor/role/", key = RoleModel.NAME, code = "0")
public class RoleCtrl {
    @Inject
    private Request request;
    @Inject
    private RoleService roleService;
}
