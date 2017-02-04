package org.lpw.ranch.audit;

import org.lpw.ranch.recycle.RecycleModel;
import org.lpw.tephra.dao.model.Model;

/**
 * 回收站Model。
 *
 * @author lpw
 */
public interface AuditModel extends Model, RecycleModel {
    /**
     * 获取审核状态。
     *
     * @return 审核状态。
     */
    int getAudit();

    /**
     * 设置审核状态。
     *
     * @param audit 审核状态。
     */
    void setAudit(int audit);

    /**
     * 获取审核备注。
     *
     * @return 审核备注。
     */
    String getAuditRemark();

    /**
     * 设置审核备注。
     *
     * @param auditRemark 审核备注。
     */
    void setAuditRemark(String auditRemark);
}
