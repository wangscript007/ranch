package org.lpw.ranch.audit;

import org.lpw.ranch.recycle.RecycleCtrlSupport;
import org.lpw.ranch.recycle.RecycleService;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;

import javax.inject.Inject;

/**
 * 回收站操作服务。
 *
 * @author lpw
 */
public abstract class AuditCtrlSupport extends RecycleCtrlSupport {
    @Inject
    protected Request request;

    /**
     * 审核通过。
     * ids ID集，多个ID间以逗号分隔。
     * auditRemark 审核备注，可空。
     *
     * @return ""。
     */
    @Execute(name = "pass", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 81),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "auditRemark", failureCode = 83),
            @Validate(validator = Validators.SIGN)
    })
    public Object pass() {
        getAuditService().pass(request.getAsArray("ids"), request.get("auditRemark"));

        return "";
    }

    /**
     * 审核不通过。
     * ids ID集，多个ID间以逗号分隔。
     * auditRemark 审核备注，必须。
     *
     * @return ""。
     */
    @Execute(name = "reject", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 81),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "auditRemark", failureCode = 82),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "auditRemark", failureCode = 83),
            @Validate(validator = Validators.SIGN)
    })
    public Object reject() {
        getAuditService().reject(request.getAsArray("ids"), request.get("auditRemark"));

        return "";
    }

    @Override
    protected RecycleService getRecycleService() {
        return getAuditService();
    }

    /**
     * 获取审核服务实例。
     *
     * @return 审核服务实例，不可为空。
     */
    protected abstract AuditService getAuditService();
}
