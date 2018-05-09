package org.lpw.ranch.editor.role;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoleService.VALIDATOR_DELETABLE)
public class DeletableValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private RoleService roleService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        if (validator.isEmpty(parameters[0]))
            return true;

        RoleModel role = roleService.find(userHelper.id(), parameters[1]);

        return role.getType() == RoleService.Type.Owner.ordinal() || parameters[0].equals(role.getUser());
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return RoleModel.NAME + ".delete.disable";
    }
}
