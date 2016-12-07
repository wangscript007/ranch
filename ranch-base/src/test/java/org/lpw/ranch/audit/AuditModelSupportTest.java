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

        model1.setAudit(Audit.Passed.getValue());
        liteOrm.save(model1);
        model2 = liteOrm.findById(TestAuditModel.class, model1.getId());
        Assert.assertEquals(Audit.Passed.getValue(), model2.getAudit());

        JSONObject object = modelHelper.toJson(model1);
        Assert.assertEquals(2, object.size());
        Assert.assertEquals(model1.getId(), object.getString("id"));
        Assert.assertEquals(Audit.Passed.getValue(), object.getInt("audit"));
    }
}
