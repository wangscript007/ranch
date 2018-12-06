package org.lpw.ranch.editor.speech;

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
@Controller(SpeechModel.NAME + ".ctrl")
@Execute(name = "/editor/speech/", key = SpeechModel.NAME, code = "32")
public class SpeechCtrl {
    @Inject
    private Request request;
    @Inject
    private SpeechService speechService;

    @Execute(name = "user", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object user() {
        return speechService.user(request.getAsArray("time"));
    }

    @Execute(name = "create", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2),
            @Validate(validator = EditorService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 11)
    })
    public Object create() {
        speechService.create(request.get("editor"));

        return "";
    }

    @Execute(name = "password", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 71),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = SpeechService.VALIDATOR_EXISTS, parameter = "id", failureCode = 72),
            @Validate(validator = SpeechService.VALIDATOR_OWNER, parameter = "id", failureCode = 73)
    })
    public Object password() {
        speechService.password(request.get("id"), request.get("password"));

        return "";
    }

    @Execute(name = "produce", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 71),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = SpeechService.VALIDATOR_EXISTS, parameter = "id", failureCode = 72),
            @Validate(validator = SpeechService.VALIDATOR_OWNER, parameter = "id", failureCode = 73)
    })
    public Object produce() {
        return speechService.produce(request.get("id"));
    }

    @Execute(name = "consume", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 71),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = SpeechService.VALIDATOR_EXISTS, parameter = "id", failureCode = 72),
            @Validate(validator = SpeechService.VALIDATOR_PASSWORD, parameters = {"id", "password"}, failureCode = 74)
    })
    public Object consume() {
        return speechService.consume(request.get("id"));
    }

    @Execute(name = "finish", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 71),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = SpeechService.VALIDATOR_EXISTS, parameter = "id", failureCode = 72),
            @Validate(validator = SpeechService.VALIDATOR_OWNER, parameter = "id", failureCode = 73)
    })
    public Object finish() {
        speechService.finish(request.get("id"));

        return "";
    }
}
