package org.lpw.ranch.audit;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.recycle.RecycleModel;
import org.lpw.ranch.recycle.RecycleModelSupportTest;

/**
 * @author lpw
 */
public class AuditModelSupportTest extends RecycleModelSupportTest {
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
        Assert.assertEquals(Audit.Passed.getValue(), object.getIntValue("audit"));
        Assert.assertEquals("remark", object.getString("auditRemark"));
    }

    @Override
    protected RecycleModel newModel() {
        return new TestAuditModel();
    }

    @Override
    protected Class<? extends RecycleModel> modelClass() {
        return TestAuditModel.class;
    }
}
