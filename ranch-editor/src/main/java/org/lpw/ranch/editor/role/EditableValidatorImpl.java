package org.lpw.ranch.editor.role;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoleService.VALIDATOR_EDITABLE)
public class EditableValidatorImpl extends ValidatorSupport {
    @Inject
    private RoleService roleService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return roleService.hasType(null, parameter, RoleService.Type.Editor);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return RoleModel.NAME + ".not-editor";
    }
}
