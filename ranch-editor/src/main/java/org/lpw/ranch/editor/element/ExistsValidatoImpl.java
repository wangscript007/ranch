package org.lpw.ranch.editor.element;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ElementService.VALIDATOR_EXISTS)
public class ExistsValidatoImpl extends ValidatorSupport {
    @Inject
    private ElementService elementService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return elementService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return ElementModel.NAME + ".not-exists";
    }
}
