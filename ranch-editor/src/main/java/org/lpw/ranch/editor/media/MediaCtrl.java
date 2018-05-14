package org.lpw.ranch.editor.media;

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
@Controller(MediaModel.NAME + ".ctrl")
@Execute(name = "/editor/media/", key = MediaModel.NAME, code = "32")
public class MediaCtrl {
    @Inject
    private Request request;
    @Inject
    private MediaService mediaService;

    @Execute(name = "query", validates = {
            @Validate(validator = EditorService.VALIDATOR_EXISTS, emptyable = true, parameter = "editor", failureCode = 62)
    })
    public Object query() {
        return mediaService.query(request.get("editor"), request.getAsInt("type", -1));
    }

    @Execute(name = "name", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "name", failureCode = 64),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 65),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 62),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 63),
            @Validate(validator = MediaService.VALIDATOR_EXISTS, parameter = "id", failureCode = 61)
    })
    public Object name() {
        return mediaService.name(request.get("id"), request.get("name"));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 62),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 63),
            @Validate(validator = MediaService.VALIDATOR_EXISTS, parameter = "id", failureCode = 61)
    })
    public Object delete() {
        mediaService.delete(request.get("id"));

        return "";
    }
}
