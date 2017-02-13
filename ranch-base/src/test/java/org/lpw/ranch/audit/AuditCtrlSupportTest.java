package org.lpw.ranch.audit;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.recycle.MockRecycleService;
import org.lpw.ranch.recycle.RecycleCtrlSupportTest;
import org.lpw.tephra.ctrl.validate.Validators;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class AuditCtrlSupportTest extends RecycleCtrlSupportTest {
    @Inject
    private TestAuditService auditService;

    @Test
    public void pass() {
        mockHelper.reset();
        mockHelper.mock("/audit/pass");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9981, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(TestAuditModel.NAME + ".ids")), object.getString("message"));
        Assert.assertNull(auditService.getPassIds());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.getRequest().addParameter("auditRemark", generator.random(101));
        mockHelper.mock("/audit/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9983, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(TestAuditModel.NAME + ".auditRemark"), 100), object.getString("message"));
        Assert.assertNull(auditService.getPassIds());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.getRequest().addParameter("auditRemark", "audit-remark");
        mockHelper.mock("/audit/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        Assert.assertNull(auditService.getPassIds());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.getRequest().addParameter("auditRemark", "audit-remark");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/audit/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertArrayEquals(new String[]{"id1", "id2"}, auditService.getPassIds());
        Assert.assertEquals("audit-remark", auditService.getAuditRemark());
    }

    @Test
    public void refuse() {
        mockHelper.reset();
        mockHelper.mock("/audit/refuse");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9981, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(TestAuditModel.NAME + ".ids")), object.getString("message"));
        Assert.assertNull(auditService.getPassIds());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.mock("/audit/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9982, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(TestAuditModel.NAME + ".auditRemark")), object.getString("message"));
        Assert.assertNull(auditService.getPassIds());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.getRequest().addParameter("auditRemark", generator.random(101));
        mockHelper.mock("/audit/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9983, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(TestAuditModel.NAME + ".auditRemark"), 100), object.getString("message"));
        Assert.assertNull(auditService.getPassIds());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.getRequest().addParameter("auditRemark", "audit-remark");
        mockHelper.mock("/audit/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        Assert.assertNull(auditService.getRefuseIds());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.getRequest().addParameter("auditRemark", "audit-remark");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/audit/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertArrayEquals(new String[]{"id1", "id2"}, auditService.getRefuseIds());
        Assert.assertEquals("audit-remark", auditService.getAuditRemark());
    }

    @Override
    protected String uriPrefix() {
        return "audit";
    }

    @Override
    protected String name() {
        return TestAuditModel.NAME;
    }

    @Override
    protected MockRecycleService recycleService() {
        return auditService;
    }
}
