package org.lpw.ranch.audit;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.TephraTestSupport;

/**
 * @author lpw
 */
public class AuditTest extends TephraTestSupport {
    @Test
    public void recycle() {
        Assert.assertEquals(0, Audit.Normal.getValue());
        Assert.assertEquals("c_audit=0", Audit.Normal.getSql());
        Assert.assertEquals(1, Audit.Passed.getValue());
        Assert.assertEquals("c_audit=1", Audit.Passed.getSql());
        Assert.assertEquals(2, Audit.Refused.getValue());
        Assert.assertEquals("c_audit=2", Audit.Refused.getSql());
        Assert.assertEquals(3, Audit.values().length);
        Assert.assertEquals(Audit.Normal, Audit.values()[0]);
        Assert.assertEquals(Audit.Passed, Audit.values()[1]);
        Assert.assertEquals(Audit.Refused, Audit.values()[2]);
        Assert.assertEquals(Audit.Normal, Audit.valueOf("Normal"));
        Assert.assertEquals(Audit.Passed, Audit.valueOf("Passed"));
        Assert.assertEquals(Audit.Refused, Audit.valueOf("Refused"));
    }
}
