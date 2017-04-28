package org.lpw.ranch.audit;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Message;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class ValidatorTest extends TephraTestSupport {
    @Inject
    private Message message;
    @Inject
    private MockHelper mockHelper;

    @Test
    public void validator() {
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "-1");
        mockHelper.mock("/audit/validator");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.audit.illegal"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "3");
        mockHelper.mock("/audit/validator");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.audit.illegal"), object.getString("message"));

        mockHelper.reset();
        mockHelper.mock("/audit/validator");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("validate success", object.getString("data"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.mock("/audit/validator");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("validate success", object.getString("data"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.mock("/audit/validator");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("validate success", object.getString("data"));
    }
}
