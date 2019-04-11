package org.lpw.ranch.editor;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(EditorService.VALIDATOR_USABLE)
public class UsableValidatorImpl extends ValidatorSupport {
    @Inject
    private EditorService editorService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return editorService.usable(parameter);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return EditorModel.NAME + ".use.disable";
    }
}
