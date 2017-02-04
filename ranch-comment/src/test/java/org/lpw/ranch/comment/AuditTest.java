package org.lpw.ranch.comment;

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
    public void pass() {
        auditTester.all(this, CommentModel.NAME, "comment", 13);
    }
}
