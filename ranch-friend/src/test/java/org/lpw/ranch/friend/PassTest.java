package org.lpw.ranch.friend;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class PassTest extends TestSupport {
    @Test
    public void pass() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/friend/pass");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1601, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(FriendModel.NAME + ".user")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.getRequest().addParameter("memo", generator.random(101));
        mockHelper.mock("/friend/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1603, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(FriendModel.NAME + ".memo"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.getRequest().addParameter("memo", "memo");
        mockHelper.mock("/friend/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.getRequest().addParameter("memo", "memo");
        mockHelper.mock("/friend/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1604, object.getIntValue("code"));
        Assert.assertEquals(message.get(FriendModel.NAME + ".not-exists"), object.getString("message"));

        FriendModel friend1 = create("sign in id", 0);
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.getRequest().addParameter("memo", "memo");
        mockHelper.mock("/friend/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1604, object.getIntValue("code"));
        Assert.assertEquals(message.get(FriendModel.NAME + ".not-exists"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        cache.remove(FriendModel.NAME + ".service.owner-user:sign in iduser 0");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user 0");
        mockHelper.getRequest().addParameter("memo", "memo 1");
        mockHelper.mock("/friend/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        FriendModel friend2 = liteOrm.findById(FriendModel.class, friend1.getId());
        Assert.assertEquals("sign in id", friend2.getOwner());
        Assert.assertEquals("user 0", friend2.getUser());
        Assert.assertEquals("memo 0", friend2.getMemo());
        Assert.assertEquals(0, friend2.getState());
        Assert.assertTrue(System.currentTimeMillis() - friend2.getCreate().getTime() < 2000L);

        friend2.setState(1);
        liteOrm.save(friend2);
        FriendModel friend3 = create("user 0", "sign in id", 0);
        cache.remove(FriendModel.NAME + ".service.owner-user:sign in iduser 0");
        cache.remove(FriendModel.NAME + ".service.owner-user:user 0sign in id");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user 0");
        mockHelper.getRequest().addParameter("memo", "new memo");
        mockHelper.mock("/friend/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        FriendModel friend4 = liteOrm.findById(FriendModel.class, friend1.getId());
        Assert.assertEquals("sign in id", friend4.getOwner());
        Assert.assertEquals("user 0", friend4.getUser());
        Assert.assertEquals("new memo", friend4.getMemo());
        Assert.assertEquals(2, friend4.getState());
        Assert.assertTrue(System.currentTimeMillis() - friend4.getCreate().getTime() < 2000L);
        FriendModel friend5 = liteOrm.findById(FriendModel.class, friend3.getId());
        Assert.assertEquals("user 0", friend5.getOwner());
        Assert.assertEquals("sign in id", friend5.getUser());
        Assert.assertEquals("memo 0", friend5.getMemo());
        Assert.assertEquals(2, friend5.getState());
        Assert.assertTrue(System.currentTimeMillis() - friend5.getCreate().getTime() < 2000L);
    }
}
