package org.lpw.ranch.editor;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(EditorService.VALIDATOR_TYPE_EXISTS)
public class TypeExistsValidatoImpl extends ValidatorSupport {
    @Inject
    private EditorService editorService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
//        return editorService.existsType(parameter);

        return true;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return EditorModel.NAME + ".type.not-exists";
    }
}
