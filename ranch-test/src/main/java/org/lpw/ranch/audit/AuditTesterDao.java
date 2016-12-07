package org.lpw.ranch.audit;

/**
 * @author lpw
 */
public interface AuditTesterDao<T extends AuditModel> {
    /**
     * 创建AuditModel持久花数据。
     *
     * @param i     序号。
     * @param audit 审核状态。
     * @return Model实例。
     */
    T create(int i, Audit audit);

    /**
     * 检索AuditModel持久花数据。
     *
     * @param id ID值。
     * @return Model实例。
     */
    T findById(String id);
}
