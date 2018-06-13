package org.lpw.ranch.editor.role;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoleService.VALIDATOR_VIEWABLE)
public class ViewableValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private RoleService roleService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return userHelper.sign().getIntValue("grade") > 50 || roleService.hasType(null, parameter, RoleService.Type.Viewer);
    }

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        return roleService.hasType(parameters[0], parameters[1], RoleService.Type.Viewer);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return RoleModel.NAME + ".not-viewer";
    }
}
