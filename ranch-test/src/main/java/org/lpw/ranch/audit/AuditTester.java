package org.lpw.ranch.audit;

/**
 * @author lpw
 */
public interface AuditTester {
    /**
     * 测试审核服务。
     *
     * @param testerDao  测试Dao。
     * @param name       Model NAME值。
     * @param uriPrefix  URI前缀。
     * @param codePrefix 错误编码前缀。
     * @param <T>        Model类。
     */
    <T extends AuditModel> void all(AuditTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix);

    /**
     * 测试审核通过服务。
     *
     * @param testerDao  测试Dao。
     * @param name       Model NAME值。
     * @param uriPrefix  URI前缀。
     * @param codePrefix 错误编码前缀。
     * @param <T>        Model类。
     */
    <T extends AuditModel> void pass(AuditTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix);

    /**
     * 测试审核不通过服务。
     *
     * @param testerDao  测试Dao。
     * @param name       Model NAME值。
     * @param uriPrefix  URI前缀。
     * @param codePrefix 错误编码前缀。
     * @param <T>        Model类。
     */
    <T extends AuditModel> void reject(AuditTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix);
}
