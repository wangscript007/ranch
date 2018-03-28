package org.lpw.ranch.editor.element;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ElementService.VALIDATOR_MODIFY)
public class ModifyValidatorImpl extends ValidatorSupport {
    @Inject
    private ElementService elementService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        return validator.isEmpty(parameters[0]) || elementService.findById(parameters[0]).getModify() == numeric.toLong(parameters[1]);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return ElementModel.NAME + ".modify.illegal";
    }
}
