package org.lpw.ranch.captcha;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.context.Response;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(CaptchaModel.NAME + ".ctrl")
@Execute(name = "/captcha/", key = CaptchaModel.NAME, code = "128")
public class CaptchaCtrl {
    @Inject
    private Request request;
    @Inject
    private Response response;
    @Inject
    private CaptchaService captchaService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return captchaService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 4),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "width", failureCode = 5),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "height", failureCode = 6),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "fontMin", failureCode = 7),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "fontMax", failureCode = 8),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "chars", failureCode = 9),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "chars", failureCode = 10),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "length", failureCode = 11),
            @Validate(validator = Validators.BETWEEN, number = {0, 1}, parameter = "background", failureCode = 12),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        return captchaService.save(request.setToModel(CaptchaModel.class));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        captchaService.delete(request.get("id"));

        return "";
    }

    @Execute(name = "image")
    public Object image() {
        response.setContentType("image/jpeg");
        captchaService.image(request.get("key"), response.getOutputStream());

        return null;
    }

    @Execute(name = "validate", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.SIGN)
    })
    public Object validate() {
        return captchaService.validate(request.get("key"), request.get("code"));
    }
}
