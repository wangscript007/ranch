package org.lpw.ranch.audit;

import java.util.Set;

/**
 * 审核操作支持类。
 *
 * @author lpw
 */
public interface AuditHelper {
    /**
     * 添加审核属性名称到集合。
     *
     * @param set 目标集合。
     */
    void addProperty(Set<String> set);

    /**
     * 设置审核通过。
     *
     * @param modelClass  Model类。
     * @param ids         ID集。
     * @param auditRemark 审核备注。
     * @param <T>         Model类。
     */
    <T extends AuditModel> void pass(Class<T> modelClass, String[] ids, String auditRemark);

    /**
     * 设置审核不通过。
     *
     * @param modelClass  Model类。
     * @param ids         ID集。
     * @param auditRemark 审核备注。
     * @param <T>         Model类。
     */
    <T extends AuditModel> void refuse(Class<T> modelClass, String[] ids, String auditRemark);
}
