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
}
