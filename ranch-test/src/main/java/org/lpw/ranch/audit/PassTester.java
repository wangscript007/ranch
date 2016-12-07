package org.lpw.ranch.audit;

/**
 * @author lpw
 */
public interface PassTester {
    /**
     * 测试审核通过服务。
     *
     * @param testerDao 测试DAO实例。
     * @param name      服务名。
     * @param code      服务编码。
     * @param <T>       Model类。
     */
    <T extends AuditModel> void pass(AuditTesterDao<T> testerDao, String name, int code);
}
