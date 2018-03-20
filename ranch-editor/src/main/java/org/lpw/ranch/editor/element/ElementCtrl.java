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
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 21),
            @Validate(validator = Validators.ID, emptyable = true, parameter = "parent", failureCode = 22),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 23),
            @Validate(validator = RoleService.VALIDATOR_VIEWABLE, parameters = {"user", "editor"}, failureCode = 11),
            @Validate(validator = ElementService.VALIDATOR_EXISTS, emptyable = true, parameter = "parent", failureCode = 24)
    })
    public Object query() {
        return elementService.query(request.get("editor"), request.get("parent"), request.getAsBoolean("recursive"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 21),
            @Validate(validator = Validators.ID, emptyable = true, parameter = "parent", failureCode = 22),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 25),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 26),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "keyword", failureCode = 27),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 23),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 10),
            @Validate(validator = ElementService.VALIDATOR_EXISTS, emptyable = true, parameter = "parent", failureCode = 24),
            @Validate(validator = ElementService.VALIDATOR_EXISTS, emptyable = true, parameter = "id", failureCode = 29),
            @Validate(validator = ElementService.VALIDATOR_EDITOR, parameters = {"id", "editor"}, failureCode = 30)
    })
    public Object save() {
        return elementService.save(request.setToModel(ElementModel.class));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 21),
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 28),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 23),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 10),
            @Validate(validator = ElementService.VALIDATOR_EXISTS, parameter = "id", failureCode = 29),
            @Validate(validator = ElementService.VALIDATOR_EDITOR, parameters = {"id", "editor"}, failureCode = 30)
    })
    public Object delete() {
        elementService.delete(request.get("id"));

        return "";
    }
}
