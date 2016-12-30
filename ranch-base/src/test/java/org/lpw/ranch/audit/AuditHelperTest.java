package org.lpw.ranch.audit;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lpw
 */
public class AuditHelperTest extends TephraTestSupport {
    @Inject
    private AuditHelper auditHelper;

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
