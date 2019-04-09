package org.lpw.ranch.editor;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(EditorService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private EditorService editorService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return editorService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return EditorModel.NAME + ".not-exists";
    }
}
