package org.lpw.ranch.editor.element;

import org.lpw.ranch.editor.EditorService;
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
@Controller(ElementModel.NAME + ".ctrl")
@Execute(name = "/editor/element/", key = ElementModel.NAME, code = "32")
public class ElementCtrl {
    @Inject
    private Request request;
    @Inject
    private ElementService elementService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = Validators.ID, emptyable = true, parameter = "parent", failureCode = 22),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2),
            @Validate(validator = RoleService.VALIDATOR_VIEWABLE, parameters = {"user", "editor"}, failureCode = 12),
            @Validate(validator = ElementService.VALIDATOR_EXISTS, emptyable = true, parameter = "parent", failureCode = 24)
    })
    public Object query() {
        return elementService.query(request.get("editor"), request.get("parent"), request.getAsBoolean("recursive"));
    }

    @Execute(name = "find", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 21),
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2),
            @Validate(validator = ElementService.VALIDATOR_EXISTS, parameter = "id", failureCode = 26),
            @Validate(validator = RoleService.VALIDATOR_VIEWABLE, parameters = {"user", "editor"}, failureCode = 12)
    })
    public Object find() {
        return elementService.find(request.get("id"), request.getAsBoolean("recursive"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = Validators.ID, emptyable = true, parameter = "parent", failureCode = 22),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2),
            @Validate(validator = EditorService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 11),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 42),
            @Validate(validator = ElementService.VALIDATOR_EXISTS, emptyable = true, parameter = "parent", failureCode = 24),
            @Validate(validator = ElementService.VALIDATOR_EXISTS, emptyable = true, parameter = "id", failureCode = 26),
            @Validate(validator = ElementService.VALIDATOR_EDITOR, parameters = {"id", "editor"}, failureCode = 27),
            @Validate(validator = ElementService.VALIDATOR_MODIFY, parameters = {"id", "modify"}, failureCode = 28)
    })
    public Object save() {
        return elementService.save(request.setToModel(ElementModel.class));
    }

    @Execute(name = "sort", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = Validators.ID, emptyable = true, parameter = "parent", failureCode = 22),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 29),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2),
            @Validate(validator = EditorService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 11),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 42),
            @Validate(validator = ElementService.VALIDATOR_EXISTS, emptyable = true, parameter = "parent", failureCode = 24)
    })
    public Object sort() {
        return elementService.sort(request.get("editor"), request.get("parent"), request.getAsArray("ids"),
                request.getAsArray("modifies"));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 25),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2),
            @Validate(validator = EditorService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 11),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 42),
            @Validate(validator = ElementService.VALIDATOR_EXISTS, parameter = "id", failureCode = 26),
            @Validate(validator = ElementService.VALIDATOR_EDITOR, parameters = {"id", "editor"}, failureCode = 27),
            @Validate(validator = ElementService.VALIDATOR_MODIFY, parameters = {"id", "modify"}, failureCode = 28)
    })
    public Object delete() {
        elementService.delete(request.get("id"));

        return "";
    }

    @Execute(name = "batch", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object batch() {
        return elementService.batch(request.getFromInputStream());
    }
}
