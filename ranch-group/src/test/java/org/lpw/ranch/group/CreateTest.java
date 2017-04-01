package org.lpw.ranch.group;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.group.member.MemberModel;
import org.lpw.ranch.group.member.MemberService;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteQuery;

/**
 * @author lpw
 */
public class CreateTest extends TestSupport {
//    @Test
    public void create() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/group/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1701, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(GroupModel.NAME + ".name")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/group/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1702, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(GroupModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", "name value");
        mockHelper.getRequest().addParameter("note", generator.random(101));
        mockHelper.mock("/group/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1703, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(GroupModel.NAME + ".note"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", "name value");
        mockHelper.getRequest().addParameter("note", "note value");
        mockHelper.getRequest().addParameter("audit", "-1");
        mockHelper.mock("/group/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1704, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(GroupModel.NAME + ".audit"), 0, 1), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", "name value");
        mockHelper.getRequest().addParameter("note", "note value");
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.mock("/group/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1704, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(GroupModel.NAME + ".audit"), 0, 1), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", "name value");
        mockHelper.getRequest().addParameter("note", "note value");
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.mock("/group/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", "name value");
        mockHelper.getRequest().addParameter("note", "note value");
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.mock("/group/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals("sign in id", data.getString("owner"));
        Assert.assertEquals("name value", data.getString("name"));
        Assert.assertEquals("note value", data.getString("note"));
        Assert.assertEquals(1, data.getIntValue("member"));
        Assert.assertEquals(1, data.getIntValue("audit"));
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toDate(data.getString("create")).getTime() < 2000L);
        GroupModel group = liteOrm.findById(GroupModel.class, data.getString("id"));
        Assert.assertEquals("sign in id", group.getOwner());
        Assert.assertEquals("name value", group.getName());
        Assert.assertEquals("note value", group.getNote());
        Assert.assertEquals(1, group.getMember());
        Assert.assertEquals(1, group.getAudit());
        Assert.assertTrue(System.currentTimeMillis() - group.getCreate().getTime() < 2000L);

        PageList<MemberModel> pl = liteOrm.query(new LiteQuery(MemberModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        MemberModel member = pl.getList().get(0);
        Assert.assertEquals(group.getId(), member.getGroup());
        Assert.assertEquals("sign in id", member.getUser());
        Assert.assertNull(member.getNick());
        Assert.assertEquals(MemberService.Type.Owner.ordinal(), member.getType());
        Assert.assertTrue(System.currentTimeMillis() - member.getJoin().getTime() < 2000L);
    }
}
