package org.lpw.ranch.audit;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class AuditDaoTest extends TephraTestSupport {
    @Autowired
    private LiteOrm liteOrm;
    @Autowired
    private AuditDao auditDao;

    @Test
    public void audit() {
        List<TestAuditModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestAuditModel model = new TestAuditModel();
            model.setAudit(i);
            model.setAuditRemark("remark " + i);
            liteOrm.save(model);
            list.add(model);
        }

        auditDao.audit(null, new String[]{list.get(0).getId(), list.get(1).getId()}, Audit.Passed, "remark");
        equals(list, 0);

        auditDao.audit(TestAuditModel.class, null, Audit.Passed, "remark");
        equals(list, 0);

        auditDao.audit(TestAuditModel.class, new String[0], Audit.Passed, "remark");
        equals(list, 0);

        auditDao.audit(TestAuditModel.class, new String[]{list.get(0).getId(), list.get(1).getId()}, null, "remark");
        equals(list, 0);

        auditDao.audit(TestAuditModel.class, new String[]{list.get(0).getId(), list.get(1).getId()}, Audit.Passed, null);
        for (int i = 0; i < 2; i++) {
            TestAuditModel model = liteOrm.findById(TestAuditModel.class, list.get(i).getId());
            Assert.assertEquals(Audit.Passed.getValue(), model.getAudit());
            Assert.assertEquals("remark " + i, model.getAuditRemark());
        }
        equals(list, 2);

        auditDao.audit(TestAuditModel.class, new String[]{list.get(0).getId(), list.get(1).getId()}, Audit.Refused, "remark");
        for (int i = 0; i < 2; i++) {
            TestAuditModel model = liteOrm.findById(TestAuditModel.class, list.get(i).getId());
            Assert.assertEquals(Audit.Refused.getValue(), model.getAudit());
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
