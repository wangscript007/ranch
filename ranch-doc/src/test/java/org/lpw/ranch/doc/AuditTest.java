package org.lpw.ranch.doc;

import org.junit.Test;
import org.lpw.ranch.audit.PassTester;
import org.lpw.ranch.audit.RefuseTester;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class AuditTest extends TestSupport {
    @Inject
    private PassTester passTester;
    @Inject
    private RefuseTester refuseTester;

    @Test
    public void pass() {
        passTester.pass(this, DocModel.NAME, "doc", 14);
    }

    @Test
    public void refuse() {
        refuseTester.refuse(this, DocModel.NAME, "doc", 14);
    }
}
