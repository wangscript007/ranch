package org.lpw.ranch.friend;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class MemoTest extends TestSupport {
    @Test
    public void memo() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/friend/memo");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1601, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(FriendModel.NAME + ".user")), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.mock("/friend/memo");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1602, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(FriendModel.NAME + ".memo")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.getRequest().addParameter("memo", generator.random(101));
        mockHelper.mock("/friend/memo");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1603, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(FriendModel.NAME + ".memo"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.getRequest().addParameter("memo", "memo");
        mockHelper.mock("/friend/memo");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.getRequest().addParameter("memo", "memo");
        mockHelper.mock("/friend/memo");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1604, object.getIntValue("code"));
        Assert.assertEquals(message.get(FriendModel.NAME + ".not-exists"), object.getString("message"));

        FriendModel friend1 = create("sign in id", 0);
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.getRequest().addParameter("memo", "memo");
        mockHelper.mock("/friend/memo");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1604, object.getIntValue("code"));
        Assert.assertEquals(message.get(FriendModel.NAME + ".not-exists"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        for (int i = 0; i < 5; i++) {
            cache.remove(FriendModel.NAME + ".service.owner-user:sign in iduser 0");
            mockHelper.reset();
            mockHelper.getRequest().addParameter("user", "user 0");
            mockHelper.getRequest().addParameter("memo", "memo " + i);
            mockHelper.mock("/friend/memo");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            Assert.assertEquals("", object.getString("data"));
            FriendModel friend2 = liteOrm.findById(FriendModel.class, friend1.getId());
            Assert.assertEquals("sign in id", friend2.getOwner());
            Assert.assertEquals("user 0", friend2.getUser());
            if (i < 2)
                Assert.assertEquals("memo 0", friend2.getMemo());
            else
                Assert.assertEquals("memo 2", friend2.getMemo());
            Assert.assertEquals(i, friend2.getState());
            Assert.assertTrue(System.currentTimeMillis() - friend2.getCreate().getTime() < 2000L);
            friend2.setState(i + 1);
            liteOrm.save(friend2);
        }
    }
}
