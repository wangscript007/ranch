package org.lpw.ranch.editor.role;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.lpw.tephra.util.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoleService.VALIDATOR_INTERVAL)
public class IntervalValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private RoleDao roleDao;
    @Value("${" + RoleModel.NAME + ".create-interval:30}")
    private int interval;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        if (!validator.isEmpty(parameter))
            return true;

        RoleModel role = roleDao.newest(userHelper.id(), RoleService.Type.Owner.ordinal());

        return role == null || System.currentTimeMillis() - role.getCreate().getTime() > interval * TimeUnit.Second.getTime();
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return RoleModel.NAME + ".frequently";
    }
}
