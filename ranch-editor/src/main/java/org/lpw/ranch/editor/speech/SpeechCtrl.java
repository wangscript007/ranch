package org.lpw.ranch.editor.speech;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.user.helper.UserHelper;
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
@Controller(SpeechModel.NAME + ".ctrl")
@Execute(name = "/editor/speech/", key = SpeechModel.NAME, code = "32")
public class SpeechCtrl {
    @Inject
    private Message message;
    @Inject
    private Request request;
    @Inject
    private Templates templates;
    @Inject
    private SpeechService speechService;

    @Execute(name = "user", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object user() {
        return speechService.user(request.getAsInt("state", -1), request.getAsArray("time"));
    }

    @Execute(name = "info", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 71),
            @Validate(validator = SpeechService.VALIDATOR_EXISTS, parameter = "id", failureCode = 72)
    })
    public Object info() {
        return speechService.info(request.get("id"));
    }

    @Execute(name = "create", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2),
            @Validate(validator = EditorService.VALIDATOR_EDITABLE, parameter = "editor", failureCode = 11)
    })
    public Object create() {
        return speechService.create(request.get("editor"));
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

    @Execute(name = "personal", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 71),
            @Validate(validator = Validators.IN, number = {0, 1}, parameter = "personal", failureCode = 75),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = SpeechService.VALIDATOR_EXISTS, parameter = "id", failureCode = 72),
            @Validate(validator = SpeechService.VALIDATOR_OWNER, parameter = "id", failureCode = 73)
    })
    public Object personal() {
        speechService.personal(request.get("id"), request.getAsInt("personal"));

        return "";
    }

    @Execute(name = "produce", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 71),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = SpeechService.VALIDATOR_EXISTS, parameter = "id", failureCode = 72),
            @Validate(validator = SpeechService.VALIDATOR_OWNER, parameter = "id", failureCode = 73)
    })
    public Object produce() {
        return entry(speechService.produce(request.get("id")));
    }

    @Execute(name = "consume", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 71),
            @Validate(validator = SpeechService.VALIDATOR_EXISTS, parameter = "id", failureCode = 72),
            @Validate(validator = SpeechService.VALIDATOR_PASSWORD, parameters = {"id", "password"}, failureCode = 74)
    })
    public Object consume() {
        return entry(speechService.consume(request.get("id")));
    }

    private Object entry(JSONObject object) {
        return object == null ? templates.get().failure(3276, message.get(SpeechModel.NAME + ".entry.failure"),
                null, null) : object;
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

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 71),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = SpeechService.VALIDATOR_EXISTS, parameter = "id", failureCode = 72),
            @Validate(validator = SpeechService.VALIDATOR_OWNER, parameter = "id", failureCode = 73)
    })
    public Object delete() {
        speechService.delete(request.get("id"));

        return "";
    }
}
