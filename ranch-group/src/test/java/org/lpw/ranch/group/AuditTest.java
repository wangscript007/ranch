package org.lpw.ranch.group;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.TimeUnit;

import java.sql.Timestamp;

/**
 * @author lpw
 */
public class AuditTest extends TestSupport {
    @Test
    public void note() {
        GroupModel group1 = create(1);
        GroupModel group2 = create(2);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "-1");
        mockHelper.mock("/group/audit");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1704, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(GroupModel.NAME + ".audit"), 0, 1), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.mock("/group/audit");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1704, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(GroupModel.NAME + ".audit"), 0, 1), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.mock("/group/audit");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1705, object.getIntValue("code"));
        Assert.assertEquals(message.get(GroupModel.NAME + ".modify.disable"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.mock("/group/audit");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1705, object.getIntValue("code"));
        Assert.assertEquals(message.get(GroupModel.NAME + ".modify.disable"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", group1.getId());
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.mock("/group/audit");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1705, object.getIntValue("code"));
        Assert.assertEquals(message.get(GroupModel.NAME + ".modify.disable"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", group2.getId());
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.mock("/group/audit");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1705, object.getIntValue("code"));
        Assert.assertEquals(message.get(GroupModel.NAME + ".modify.disable"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"owner 1\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", group2.getId());
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.mock("/group/audit");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1705, object.getIntValue("code"));
        Assert.assertEquals(message.get(GroupModel.NAME + ".modify.disable"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", group1.getId());
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.mock("/group/audit");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(group1.getId(), data.getString("id"));
        Assert.assertEquals("owner 1", data.getString("owner"));
        Assert.assertEquals("name 1", data.getString("name"));
        Assert.assertEquals("note 1", data.getString("note"));
        Assert.assertEquals(101, data.getIntValue("member"));
        Assert.assertEquals(0, data.getIntValue("audit"));
        Assert.assertEquals(converter.toString(new Timestamp(now - TimeUnit.Day.getTime())), data.getString("create"));
        GroupModel group11 = liteOrm.findById(GroupModel.class, group1.getId());
        Assert.assertEquals("owner 1", group11.getOwner());
        Assert.assertEquals("name 1", group11.getName());
        Assert.assertEquals("note 1", group11.getNote());
        Assert.assertEquals(101, group11.getMember());
        Assert.assertEquals(0, group11.getAudit());
        Assert.assertEquals(converter.toString(new Timestamp(now - TimeUnit.Day.getTime())), converter.toString(group11.getCreate()));
        GroupModel group22 = liteOrm.findById(GroupModel.class, group2.getId());
        Assert.assertEquals("owner 2", group22.getOwner());
        Assert.assertEquals("name 2", group22.getName());
        Assert.assertEquals("note 2", group22.getNote());
        Assert.assertEquals(102, group22.getMember());
        Assert.assertEquals(2, group22.getAudit());
        Assert.assertEquals(converter.toString(new Timestamp(now - 2 * TimeUnit.Day.getTime())), converter.toString(group22.getCreate()));
    }
}
