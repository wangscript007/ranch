package org.lpw.ranch.audit;

import org.lpw.ranch.recycle.RecycleService;

/**
 * 回收站服务。
 *
 * @author lpw
 */
public interface AuditService extends RecycleService {
    /**
     * 审核通过。
     *
     * @param ids         ID集。
     * @param auditRemark 审核备注。
     */
    void pass(String[] ids, String auditRemark);

    /**
     * 审核不通过。
     *
     * @param ids         ID集。
     * @param auditRemark 审核备注。
     */
    void reject(String[] ids, String auditRemark);
}
