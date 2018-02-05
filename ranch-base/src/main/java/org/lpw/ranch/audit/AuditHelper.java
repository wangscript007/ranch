package org.lpw.ranch.audit;

import java.util.Set;

/**
 * 审核操作支持类。
 *
 * @author lpw
 */
public interface AuditHelper {
    /**
     * 审核状态值验证器Bean名称。
     * 默认错误信息key=ranch.audit.illegal。
     */
    String VALIDATOR = "ranch.audit.validator";

    /**
     * 获取审核状态。
     *
     * @param value 状态值。
     * @return 审核状态，不存在则返回null。
     */
    Audit get(int value);

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
    <T extends AuditModel> void reject(Class<T> modelClass, String[] ids, String auditRemark);
}
