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
            liteOrm.save(model);
            list.add(model);
        }

        auditDao.audit(null, new String[]{list.get(0).getId(), list.get(1).getId()}, Audit.Passed);
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestAuditModel.class, list.get(i).getId()).getAudit());

        auditDao.audit(TestAuditModel.class, null, Audit.Passed);
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestAuditModel.class, list.get(i).getId()).getAudit());

        auditDao.audit(TestAuditModel.class, new String[0], Audit.Passed);
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestAuditModel.class, list.get(i).getId()).getAudit());

        auditDao.audit(TestAuditModel.class, new String[]{list.get(0).getId(), list.get(1).getId()}, null);
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestAuditModel.class, list.get(i).getId()).getAudit());

        auditDao.audit(TestAuditModel.class, new String[]{list.get(0).getId(), list.get(1).getId()}, Audit.Refused);
        for (int i = 0; i < 2; i++)
            Assert.assertEquals(Audit.Refused.getValue(), liteOrm.findById(TestAuditModel.class, list.get(i).getId()).getAudit());
        for (int i = 2; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestAuditModel.class, list.get(i).getId()).getAudit());
    }
}
