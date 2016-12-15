package org.lpw.ranch.audit;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.TephraTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lpw
 */
public class AuditHelperTest extends TephraTestSupport {
    @Autowired
    protected AuditHelper auditHelper;

    @Test
    public void addProperty() {
        Set<String> set = null;
        auditHelper.addProperty(set);

        set = new HashSet<>();
        auditHelper.addProperty(set);
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.contains("audit"));
        Assert.assertTrue(set.contains("auditRemark"));
    }
}
