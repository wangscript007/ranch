package org.lpw.ranch.audit;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class AuditModelSupportTest extends TephraTestSupport {
    @Autowired
    private ModelHelper modelHelper;
    @Autowired
    private LiteOrm liteOrm;

    @Test
    public void audit() {
        TestAuditModel model1 = new TestAuditModel();
        model1.setAudit(Audit.Normal.getValue());
        liteOrm.save(model1);
        TestAuditModel model2 = liteOrm.findById(TestAuditModel.class, model1.getId());
        Assert.assertEquals(Audit.Normal.getValue(), model2.getAudit());
        Assert.assertNull(model2.getAuditRemark());

        model1.setAudit(Audit.Passed.getValue());
        model1.setAuditRemark("remark");
        liteOrm.save(model1);
        model2 = liteOrm.findById(TestAuditModel.class, model1.getId());
        Assert.assertEquals(Audit.Passed.getValue(), model2.getAudit());
        Assert.assertEquals("remark", model2.getAuditRemark());

        JSONObject object = modelHelper.toJson(model1);
        Assert.assertEquals(3, object.size());
        Assert.assertEquals(model1.getId(), object.getString("id"));
        Assert.assertEquals(Audit.Passed.getValue(), object.getInt("audit"));
        Assert.assertEquals("remark", object.getString("auditRemark"));
    }
}
