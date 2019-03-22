package org.lpw.ranch.editor.screenshot;

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
@Controller(ScreenshotModel.NAME + ".ctrl")
@Execute(name = "/editor/screenshot/", key = ScreenshotModel.NAME, code = "32")
public class ScreenshotCtrl {
    @Inject
    private Request request;
    @Inject
    private ScreenshotService screenshotService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2)
    })
    public Object query() {
        return screenshotService.query(request.get("editor"));
    }

    @Execute(name = "find", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2)
    })
    public Object find() {
        return screenshotService.find(request.get("editor"), request.get("page"));
    }

    @Execute(name = "capture", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "mainWidth", failureCode = 82),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "mainHeight", failureCode = 83),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "pageWidth", failureCode = 84),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "pageHeight", failureCode = 85),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = UserHelper.VALIDATOR_GRADE, number = {50, 99}, failureCode = 84),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2)
    })
    public Object capture() {
        return screenshotService.capture(request.get("editor"), request.getAsInt("mainWidth"),
                request.getAsInt("mainHeight"), request.getAsInt("pageWidth"), request.getAsInt("pageHeight"));
    }

    @Execute(name = "images", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2),
            @Validate(validator = EditorService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 11),
            @Validate(validator = RoleService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 41)
    })
    public Object images(){
        return screenshotService.capture(request.get("editor"));
    }
}
