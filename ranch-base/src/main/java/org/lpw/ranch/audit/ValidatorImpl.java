package org.lpw.ranch.audit;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

/**
 * @author lpw
 */
@Controller(AuditHelper.VALIDATOR)
public class ValidatorImpl extends ValidatorSupport {
    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        int n = converter.toInt(parameter);

        return n >= Audit.Normal.ordinal() && n <= Audit.Refused.ordinal();
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.audit.illegal";
    }
}
