package org.lpw.ranch.audit;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.util.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class AuditCtrlSupportTest extends TephraTestSupport {
    @Autowired
    private Message message;
    @Autowired
    private Sign sign;
    @Autowired
    private MockHelper mockHelper;
    @Autowired
    private TestAuditService auditService;

    @Test
    public void pass() {
        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.mock("/audit/pass");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9991, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        Assert.assertNull(auditService.getPassIds());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/audit/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertArrayEquals(new String[]{"id1", "id2"}, auditService.getPassIds());
    }

    @Test
    public void refuse() {
        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.mock("/audit/refuse");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9991, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        Assert.assertNull(auditService.getRefuseIds());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/audit/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertArrayEquals(new String[]{"id1", "id2"}, auditService.getRefuseIds());
    }
}
