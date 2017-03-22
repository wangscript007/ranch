package org.lpw.ranch.message;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MessageService.VALIDATOR_NOT_EXISTS_CODE)
public class CodeNotExistsValidatorImpl extends ValidatorSupport {
    @Inject private MessageService messageService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return !messageService.exists(parameter);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return MessageModel.NAME+".code.exists";
    }
}
