package org.lpw.ranch.audit;

import org.lpw.tephra.dao.model.Model;

/**
 * 回收站Model。
 *
 * @author lpw
 */
public interface AuditModel extends Model {
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
}
