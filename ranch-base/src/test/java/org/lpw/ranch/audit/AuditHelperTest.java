package org.lpw.ranch.audit;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lpw
 */
public class AuditHelperTest extends TephraTestSupport {
    @Inject
    private LiteOrm liteOrm;
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

    @Test
    public void pass() {
        List<TestAuditModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestAuditModel model = new TestAuditModel();
            model.setAudit(i);
            model.setAuditRemark("remark " + i);
            liteOrm.save(model);
            list.add(model);
        }

        auditHelper.pass(null, new String[]{list.get(0).getId(), list.get(1).getId()}, "remark");
        equals(list, 0);

        auditHelper.pass(TestAuditModel.class, null, "remark");
        equals(list, 0);

        auditHelper.pass(TestAuditModel.class, new String[0], "remark");
        equals(list, 0);

        auditHelper.pass(TestAuditModel.class, new String[]{list.get(0).getId(), list.get(1).getId()}, null);
        for (int i = 0; i < 2; i++) {
            TestAuditModel model = liteOrm.findById(TestAuditModel.class, list.get(i).getId());
            Assert.assertEquals(Audit.Pass.getValue(), model.getAudit());
            Assert.assertEquals("remark " + i, model.getAuditRemark());
        }
        equals(list, 2);
    }

    @Test
    public void refuse() {
        List<TestAuditModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestAuditModel model = new TestAuditModel();
            model.setAudit(i);
            model.setAuditRemark("remark " + i);
            liteOrm.save(model);
            list.add(model);
        }

        auditHelper.refuse(null, new String[]{list.get(0).getId(), list.get(1).getId()}, "remark");
        equals(list, 0);

        auditHelper.refuse(TestAuditModel.class, null, "remark");
        equals(list, 0);

        auditHelper.refuse(TestAuditModel.class, new String[0], "remark");
        equals(list, 0);

        auditHelper.refuse(TestAuditModel.class, new String[]{list.get(0).getId(), list.get(1).getId()}, "remark");
        for (int i = 0; i < 2; i++) {
            TestAuditModel model = liteOrm.findById(TestAuditModel.class, list.get(i).getId());
            Assert.assertEquals(Audit.Reject.getValue(), model.getAudit());
            Assert.assertEquals("remark", model.getAuditRemark());
        }
        equals(list, 2);
    }

    private void equals(List<TestAuditModel> list, int i) {
        for (; i < list.size(); i++) {
            TestAuditModel model = liteOrm.findById(TestAuditModel.class, list.get(i).getId());
            Assert.assertEquals(i, model.getAudit());
            Assert.assertEquals("remark " + i, model.getAuditRemark());
        }
    }
}
