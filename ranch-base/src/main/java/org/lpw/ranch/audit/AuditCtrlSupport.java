package org.lpw.ranch.audit;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 回收站操作服务。
 *
 * @author lpw
 */
public abstract class AuditCtrlSupport {
    @Autowired
    protected Request request;

    /**
     * 审核通过。
     * ids ID集，多个ID间以逗号分隔。
     *
     * @return ""。
     */
    @Execute(name = "pass", validates = {@Validate(validator = Validators.SIGN)})
    public Object pass() {
        getAuditService().pass(request.getAsArray("ids"));

        return "";
    }

    /**
     * 审核不通过。
     * ids ID集，多个ID间以逗号分隔。
     *
     * @return ""。
     */
    @Execute(name = "refuse", validates = {@Validate(validator = Validators.SIGN)})
    public Object refuse() {
        getAuditService().refuse(request.getAsArray("ids"));

        return "";
    }

    /**
     * 获取审核服务实例。
     *
     * @return 审核服务实例，不可为空。
     */
    protected abstract AuditService getAuditService();
}
