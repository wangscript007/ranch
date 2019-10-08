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
@Execute(name = "/editor/screenshot/", key = ScreenshotModel.NAME, code = "132")
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
}
