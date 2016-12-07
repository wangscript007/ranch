package org.lpw.ranch.audit;

/**
 * @author lpw
 */
public interface AuditDao {
    /**
     * 设置审核状态。
     *
     * @param modelClass Model类。
     * @param ids        ID集。
     * @param audit      目标状态。
     * @param <T>        Model类。
     */
    <T extends AuditModel> void audit(Class<T> modelClass, String[] ids, Audit audit);
}
