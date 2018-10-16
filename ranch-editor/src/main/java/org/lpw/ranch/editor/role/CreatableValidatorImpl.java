package org.lpw.ranch.editor.role;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoleService.VALIDATOR_CREATABLE)
public class CreatableValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private RoleDao roleDao;
    @Value("${" + RoleModel.NAME + ".free-create:5}")
    private int freeCreate;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return !validator.isEmpty(parameter) || userHelper.isVip()
                || roleDao.count(userHelper.id(), RoleService.Type.Owner.ordinal()) <= freeCreate;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return RoleModel.NAME + ".create.disable";
    }
}
