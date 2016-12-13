package org.lpw.ranch.comment;

import org.junit.Test;
import org.lpw.ranch.audit.PassTester;
import org.lpw.ranch.audit.RefuseTester;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class AuditTest extends TestSupport {
    @Autowired
    private PassTester passTester;
    @Autowired
    private RefuseTester refuseTester;

    @Test
    public void pass() {
        passTester.pass(this, CommentModel.NAME, "comment", 13);
    }

    @Test
    public void refuse() {
        refuseTester.refuse(this, CommentModel.NAME, "comment", 13);
    }
}
