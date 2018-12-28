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
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        return elementService.findById(parameters[0], parameters[1]) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return ElementModel.NAME + ".not-exists";
    }
}
