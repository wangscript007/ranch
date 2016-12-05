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
     * @param ids ID集。
     */
    void pass(String[] ids);

    /**
     * 审核不通过。
     *
     * @param ids ID集。
     */
    void refuse(String[] ids);
}
