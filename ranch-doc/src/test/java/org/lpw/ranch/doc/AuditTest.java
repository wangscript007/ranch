package org.lpw.ranch.doc;

import org.junit.Test;
import org.lpw.ranch.audit.AuditTester;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class AuditTest extends TestSupport {
    @Inject
    private AuditTester auditTester;

    @Test
    public void all() {
        auditTester.all(this, DocModel.NAME, "doc", 14);
    }
}
