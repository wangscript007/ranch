package org.lpw.ranch.audit;

/**
 * @author lpw
 */
public interface RefuseTester {
    /**
     * 测试审核不通过服务。
     *
     * @param testerDao 测试DAO实例。
     * @param name      服务名。
     * @param code      服务编码。
     * @param <T>       Model类。
     */
    <T extends AuditModel> void refuse(AuditTesterDao<T> testerDao, String name, int code);
}
