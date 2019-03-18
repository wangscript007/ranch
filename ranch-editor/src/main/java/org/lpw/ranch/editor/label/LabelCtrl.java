package org.lpw.ranch.editor.label;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(LabelModel.NAME + ".ctrl")
@Execute(name = "/editor/label/", key = LabelModel.NAME, code = "32")
public class LabelCtrl {
    @Inject
    private Request request;
    @Inject
    private LabelService labelService;

    @Execute(name = "rename", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "oldName", failureCode = 51),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "newName", failureCode = 52),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "newName", failureCode = 53),
            @Validate(validator = Validators.SIGN)
    })
    public Object rename() {
        labelService.rename(request.get("oldName"), request.get("newName"));

        return "";
    }
}
