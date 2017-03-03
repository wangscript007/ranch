package org.lpw.ranch.group.member;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.group.GroupModel;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.TimeUnit;

/**
 * @author lpw
 */
public class JoinTest extends TestSupport {
    @Test
    public void join() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("reason", generator.random(101));
        mockHelper.mock("/group/member/join");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1721, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(MemberModel.NAME + ".reason"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("reason", "join reason");
        mockHelper.mock("/group/member/join");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1722, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.group.not-exists"), object.getString("message"));

        GroupModel group1 = new GroupModel();
        group1.setOwner("owner id 1");
        group1.setCreate(dateTime.now());
        liteOrm.save(group1);
        GroupModel group2 = new GroupModel();
        group2.setOwner("owner id 2");
        group2.setAudit(1);
        group2.setCreate(dateTime.now());
        liteOrm.save(group2);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", generator.uuid());
        mockHelper.getRequest().addParameter("reason", "join reason");
        mockHelper.mock("/group/member/join");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1722, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.group.not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", group1.getId());
        mockHelper.getRequest().addParameter("reason", "join reason");
        mockHelper.mock("/group/member/join");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", group1.getId());
        mockHelper.getRequest().addParameter("reason", "join reason 1");
        mockHelper.mock("/group/member/join");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        PageList<MemberModel> pl = liteOrm.query(new LiteQuery(MemberModel.class).where("c_group=?"), new Object[]{group1.getId()});
        Assert.assertEquals(1, pl.getList().size());
        MemberModel member1 = pl.getList().get(0);
        Assert.assertEquals(group1.getId(), member1.getGroup());
        Assert.assertEquals("sign in id", member1.getUser());
        Assert.assertNull(member1.getNick());
        Assert.assertEquals("join reason 1", member1.getReason());
        Assert.assertEquals(MemberService.Type.Normal.ordinal(), member1.getType());
        Assert.assertTrue(System.currentTimeMillis() - member1.getJoin().getTime() < 2000L);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", group2.getId());
        mockHelper.getRequest().addParameter("reason", "join reason 2");
        mockHelper.mock("/group/member/join");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        pl = liteOrm.query(new LiteQuery(MemberModel.class).where("c_group=?"), new Object[]{group2.getId()});
        Assert.assertEquals(1, pl.getList().size());
        MemberModel member2 = pl.getList().get(0);
        Assert.assertEquals(group2.getId(), member2.getGroup());
        Assert.assertEquals("sign in id", member2.getUser());
        Assert.assertNull(member2.getNick());
        Assert.assertEquals("join reason 2", member2.getReason());
        Assert.assertEquals(MemberService.Type.New.ordinal(), member2.getType());
        Assert.assertTrue(System.currentTimeMillis() - member2.getJoin().getTime() < 2000L);

        thread.sleep(3, TimeUnit.Second);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", group2.getId());
        mockHelper.getRequest().addParameter("reason", "join reason 3");
        mockHelper.mock("/group/member/join");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        pl = liteOrm.query(new LiteQuery(MemberModel.class).where("c_group=?"), new Object[]{group2.getId()});
        Assert.assertEquals(1, pl.getList().size());
        MemberModel member3 = pl.getList().get(0);
        Assert.assertEquals(group2.getId(), member3.getGroup());
        Assert.assertEquals("sign in id", member3.getUser());
        Assert.assertNull(member3.getNick());
        Assert.assertEquals("join reason 3", member3.getReason());
        Assert.assertEquals(MemberService.Type.New.ordinal(), member3.getType());
        Assert.assertTrue(System.currentTimeMillis() - member3.getJoin().getTime() > 2000L);
    }
}
