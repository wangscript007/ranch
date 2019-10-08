package org.lpw.ranch.editor.role;

import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoleModel.NAME + ".ctrl")
@Execute(name = "/editor/role/", key = RoleModel.NAME, code = "132")
public class RoleCtrl {
    @Inject
    private Request request;
    @Inject
    private EditorService editorService;
    @Inject
    private RoleService roleService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 41),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 42),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 43)
    })
    public Object query() {
        return roleService.query(request.get("editor"));
    }

    @Execute(name = "share", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "password", failureCode = 46),
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 41),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 42),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 43)
    })
    public Object share() {
        return roleService.share(request.get("editor"), request.get("password"));
    }

    @Execute(name = "password", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 45),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "password", failureCode = 46),
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 41),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 42),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 43),
            @Validate(validator = RoleService.VALIDATOR_EXISTS, parameters = {"id", "editor"}, failureCode = 47)
    })
    public Object password() {
        roleService.password(request.get("id"), request.get("password"));

        return "";
    }

    @Execute(name = "need-password", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 41),
    })
    public Object needPassword() {
        return roleService.needPassword(request.get("user"), request.get("editor"));
    }

    @Execute(name = "count-owner", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object countOwner() {
        return roleService.countOwner();
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 41),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 42),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 43),
            @Validate(validator = RoleService.VALIDATOR_DELETABLE, parameters = {"user", "editor"}, failureCode = 44),
    })
    public Object delete() {
        roleService.delete(request.get("user"), request.get("editor"));

        return "";
    }

    @Execute(name = "restore", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 41),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 42),
            @Validate(validator = RoleService.VALIDATOR_OWNER, parameter = "editor", failureCode = 43)
    })
    public Object restore() {
        editorService.restore(request.get("editor"));

        return "";
    }

    @Execute(name = "remove", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 41),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 42),
            @Validate(validator = RoleService.VALIDATOR_OWNER, parameter = "editor", failureCode = 43)
    })
    public Object remove() {
        roleService.remove(request.get("editor"));

        return "";
    }
}
