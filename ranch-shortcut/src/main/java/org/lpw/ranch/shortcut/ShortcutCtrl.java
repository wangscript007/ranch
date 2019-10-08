package org.lpw.ranch.shortcut;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Message;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ShortcutModel.NAME + ".ctrl")
@Execute(name = "/shortcut/", key = ShortcutModel.NAME, code = "135")
public class ShortcutCtrl {
    @Inject
    private Message message;
    @Inject
    private Request request;
    @Inject
    private Templates templates;
    @Inject
    private ShortcutService shortcutService;

    @Execute(name = "find", validates = {
            @Validate(validator = ShortcutService.VALIDATOR_CODE_EXISTS, parameter = "code", failureCode = 1)
    })
    public Object find() {
        return shortcutService.find(request.get("code"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.BETWEEN, number = {8, 250}, parameter = "length", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "value", failureCode = 3)
    })
    public Object save() {
        String code = shortcutService.save(request.getAsInt("length"), request.get("value"));

        return code == null ? templates.get().failure(3504,
                message.get(ShortcutModel.NAME + ".code.failure"), null, null) : code;
    }
}
