package org.lpw.ranch.editor;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(EditorService.VALIDATOR_COPY)
public class CopyValidatorImpl extends ValidatorSupport {
    @Inject
    private EditorService editorService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return editorService.isTemplateOwner(parameter);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return EditorModel.NAME + ".copy.disable";
    }
}
