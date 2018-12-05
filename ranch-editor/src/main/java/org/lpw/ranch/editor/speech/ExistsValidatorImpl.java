package org.lpw.ranch.editor.speech;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller(SpeechService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private SpeechService speechService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return speechService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return SpeechModel.NAME + ".not-exists";
    }
}
