package org.lpw.ranch.group.member;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.group.GroupModel;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class NickTest extends TestSupport {
    @Test
    public void pass() {
        GroupModel group = new GroupModel();
        group.setOwner("group owner");
        group.setCreate(dateTime.now());
        liteOrm.save(group);
        MemberModel member1 = new MemberModel();
        member1.setGroup(group.getId());
        member1.setUser("user 1");
        member1.setJoin(dateTime.now());
        liteOrm.save(member1);
        MemberModel member2 = new MemberModel();
        member2.setGroup(group.getId());
        member2.setUser("user 2");
        member2.setType(MemberService.Type.Normal.ordinal());
        member2.setJoin(dateTime.now());
        liteOrm.save(member2);
        MemberModel member3 = new MemberModel();
        member3.setGroup(group.getId());
        member3.setUser("user 3");
        member3.setType(MemberService.Type.Manager.ordinal());
        member3.setJoin(dateTime.now());
        liteOrm.save(member3);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/group/member/nick");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1723, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(MemberModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.getRequest().addParameter("nick", generator.random(101));
        mockHelper.mock("/group/member/nick");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1726, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(MemberModel.NAME + ".nick"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/group/member/nick");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 2\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/group/member/nick");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1724, object.getIntValue("code"));
        Assert.assertEquals(message.get(MemberModel.NAME + ".not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", member1.getId());
        mockHelper.getRequest().addParameter("nick", "new nick");
        mockHelper.mock("/group/member/nick");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1727, object.getIntValue("code"));
        Assert.assertEquals(message.get(MemberModel.NAME + ".not-self-manager"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 0\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", member1.getId());
        mockHelper.getRequest().addParameter("nick", "new nick");
        mockHelper.mock("/group/member/nick");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1727, object.getIntValue("code"));
        Assert.assertEquals(message.get(MemberModel.NAME + ".not-self-manager"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 3\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", member1.getId());
        mockHelper.getRequest().addParameter("nick", "new nick");
        mockHelper.mock("/group/member/nick");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        MemberModel member11 = liteOrm.findById(MemberModel.class, member1.getId());
        Assert.assertEquals("new nick", member11.getNick());

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 1\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", member1.getId());
        mockHelper.getRequest().addParameter("nick", "nick 111");
        mockHelper.mock("/group/member/nick");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        MemberModel member111 = liteOrm.findById(MemberModel.class, member1.getId());
        Assert.assertEquals("nick 111", member111.getNick());
    }
}
