package org.lpw.ranch.editor.speech;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller(SpeechService.VALIDATOR_PASSWORD)
public class PasswordValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private SpeechService speechService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        SpeechModel speech = speechService.findById(parameters[0]);

        return validator.isEmpty(speech.getPassword()) || speech.getPassword().equals(parameters[1]) || speech.getUser().equals(userHelper.id());
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return SpeechModel.NAME + ".password.illegal";
    }
}
