package org.lpw.ranch.audit;

/**
 * 回收站服务。
 *
 * @author lpw
 */
public interface AuditService {
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
    void refuse(String[] ids, String auditRemark);
}
