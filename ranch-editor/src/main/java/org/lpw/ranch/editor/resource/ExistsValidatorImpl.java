package org.lpw.ranch.editor.resource;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ResourceService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private ResourceService resourceService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return resourceService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return ResourceModel.NAME + ".not-exists";
    }
}
