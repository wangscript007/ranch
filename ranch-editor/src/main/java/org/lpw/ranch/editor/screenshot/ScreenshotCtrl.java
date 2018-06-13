package org.lpw.ranch.editor.screenshot;

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
            @Validate(validator = Validators.NOT_EMPTY, parameter = "pages", failureCode = 81),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "mainWidth", failureCode = 82),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "mainHeight", failureCode = 83),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "pageWidth", failureCode = 84),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "pageHeight", failureCode = 85),
            @Validate(validator = UserHelper.VALIDATOR_GRADE, number = {50, 99}, failureCode = 84),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2)
    })
    public Object capture() {
        return screenshotService.capture(request.get("editor"), request.getAsArray("pages"), request.getAsInt("mainWidth"),
                request.getAsInt("mainHeight"), request.getAsInt("pageWidth"), request.getAsInt("pageHeight"));
    }
}
