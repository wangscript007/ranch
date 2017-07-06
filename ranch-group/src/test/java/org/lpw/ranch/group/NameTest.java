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
public class NameTest extends TestSupport {
    @Test
    public void name() {
        GroupModel group1 = create(1);
        GroupModel group2 = create(2);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/group/name");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1701, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(GroupModel.NAME + ".name")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/group/name");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1702, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(GroupModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.mock("/group/name");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1705, object.getIntValue("code"));
        Assert.assertEquals(message.get(GroupModel.NAME + ".need-owner"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.mock("/group/name");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1705, object.getIntValue("code"));
        Assert.assertEquals(message.get(GroupModel.NAME + ".need-owner"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", group1.getId());
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.mock("/group/name");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1705, object.getIntValue("code"));
        Assert.assertEquals(message.get(GroupModel.NAME + ".need-owner"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", group2.getId());
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.mock("/group/name");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1705, object.getIntValue("code"));
        Assert.assertEquals(message.get(GroupModel.NAME + ".need-owner"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"owner 1\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", group2.getId());
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.mock("/group/name");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1705, object.getIntValue("code"));
        Assert.assertEquals(message.get(GroupModel.NAME + ".need-owner"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", group1.getId());
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.mock("/group/name");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(group1.getId(), data.getString("id"));
        Assert.assertEquals("owner 1", data.getString("owner"));
        Assert.assertEquals("new name", data.getString("name"));
        Assert.assertEquals("note 1", data.getString("note"));
        Assert.assertEquals(101, data.getIntValue("member"));
        Assert.assertEquals(1, data.getIntValue("audit"));
        Assert.assertEquals(converter.toString(new Timestamp(now - TimeUnit.Day.getTime())), data.getString("create"));
        GroupModel group11 = liteOrm.findById(GroupModel.class, group1.getId());
        Assert.assertEquals("owner 1", group11.getOwner());
        Assert.assertEquals("new name", group11.getName());
        Assert.assertEquals("note 1", group11.getNote());
        Assert.assertEquals(101, group11.getMember());
        Assert.assertEquals(1, group11.getAudit());
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
