package org.lpw.ranch.audit;

/**
 * @author lpw
 */
public interface TestAuditService extends AuditService {
    String[] getPassIds();

    String[] getRefuseIds();

    String getAuditRemark();
}
