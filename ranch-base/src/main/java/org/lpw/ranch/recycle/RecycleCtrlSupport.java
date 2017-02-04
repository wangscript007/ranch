package org.lpw.ranch.recycle;

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
public abstract class RecycleCtrlSupport {
    @Inject
    protected Request request;

    /**
     * 删除信息到回收站。
     * id ID值。
     *
     * @return ""。
     */
    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 86),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        getRecycleService().delete(request.get("id"));

        return "";
    }

    /**
     * 检索回收站信息集。
     * pageSize 每页显示最大记录数。
     * pageNum 当前显示页码。
     *
     * @return {PageList}。
     */
    @Execute(name = "recycle", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object recycle() {
        return getRecycleService().recycle();
    }

    /**
     * （从回收站）还原数据。
     * id ID值。
     *
     * @return ""。
     */
    @Execute(name = "restore", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 86),
            @Validate(validator = Validators.SIGN)
    })
    public Object restore() {
        getRecycleService().restore(request.get("id"));

        return "";
    }

    /**
     * 获取回收站服务实例。
     *
     * @return 回收站服务实例，不可为空。
     */
    protected abstract RecycleService getRecycleService();
}
