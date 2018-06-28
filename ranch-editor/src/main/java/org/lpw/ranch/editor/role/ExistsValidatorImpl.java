package org.lpw.ranch.editor.role;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoleService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private RoleService roleService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        RoleModel role = roleService.findById(parameters[0]);

        return role != null && role.getEditor().equals(parameters[1]);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return RoleModel.NAME + ".not-exists";
    }
}
