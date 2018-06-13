package org.lpw.ranch.resource;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ResourceService.VALIDATOR_EDITABLE)
public class EditableValidatorImpl extends ValidatorSupport {
    @Inject
    private ResourceService resourceService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return resourceService.findById(parameter).getState() != 3;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return ResourceModel.NAME + ".edit.disable";
    }
}
