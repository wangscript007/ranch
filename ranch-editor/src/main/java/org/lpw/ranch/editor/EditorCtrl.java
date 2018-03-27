package org.lpw.ranch.editor;

import org.lpw.ranch.editor.role.RoleService;
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
@Controller(EditorModel.NAME + ".ctrl")
@Execute(name = "/editor/", key = EditorModel.NAME, code = "32")
public class EditorCtrl {
    @Inject
    private Request request;
    @Inject
    private EditorService editorService;

    @Execute(name = "query-user", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object queryUser() {
        return editorService.queryUser();
    }

    @Execute(name = "find", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "id", failureCode = 2),
            @Validate(validator = RoleService.VALIDATOR_VIEWABLE, parameters = {"user", "id"}, failureCode = 12)
    })
    public Object find() {
        return editorService.find(request.get("id"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.ID, emptyable = true, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 4),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "name", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "label", failureCode = 7),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "width", failureCode = 8),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "height", failureCode = 9),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "image", failureCode = 10),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, emptyable = true, parameter = "id", failureCode = 2),
            @Validate(validator = RoleService.VALIDATOR_OWNER, emptyable = true, parameter = "id", failureCode = 11)
    })
    public Object save() {
        return editorService.save(request.setToModel(EditorModel.class));
    }

    @Execute(name = "copy", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 4),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "id", failureCode = 2)
    })
    public Object copy() {
        return editorService.copy(request.get("id"), request.get("type"));
    }
}
