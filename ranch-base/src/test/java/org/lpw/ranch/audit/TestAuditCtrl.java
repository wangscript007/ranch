package org.lpw.ranch.audit;

import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(TestAuditModel.NAME + ".ctrl")
@Execute(name = "/audit/", key = TestAuditModel.NAME, code = "99")
public class TestAuditCtrl extends AuditCtrlSupport {
    @Inject
    private TestAuditService auditService;

    @Execute(name = "validator", validates = {
            @Validate(validator = AuditHelper.VALIDATOR, parameter = "audit", failureCode = 1)
    })
    public Object validator() {
        return "validate success";
    }

    @Override
    protected AuditService getAuditService() {
        return auditService;
    }
}
