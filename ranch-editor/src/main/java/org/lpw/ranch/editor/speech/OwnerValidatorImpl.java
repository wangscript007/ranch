package org.lpw.ranch.editor.speech;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller(SpeechService.VALIDATOR_OWNER)
public class OwnerValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private SpeechService speechService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return speechService.findById(parameter).getUser().equals(userHelper.id());
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return SpeechModel.NAME + ".not-owner";
    }
}
