package org.lpw.ranch.friend;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteQuery;

/**
 * @author lpw
 */
public class CreateTest extends TestSupport {
    @Test
    public void create() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/friend/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1601, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(FriendModel.NAME + ".user")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.getRequest().addParameter("memo", generator.random(101));
        mockHelper.mock("/friend/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1603, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(FriendModel.NAME + ".memo"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user");
        mockHelper.getRequest().addParameter("memo", "memo");
        mockHelper.mock("/friend/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"user id\":{\"id\":\"user id\",\"name\":\"user name\"}}}");
        for (int i = 0; i < 5; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("user", "user id");
            mockHelper.getRequest().addParameter("memo", "memo");
            mockHelper.mock("/friend/create");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            Assert.assertEquals("", object.getString("data"));
            PageList<FriendModel> pl = liteOrm.query(new LiteQuery(FriendModel.class).order("c_user desc"), null);
            Assert.assertEquals(2, pl.getList().size());
            FriendModel friend = pl.getList().get(0);
            Assert.assertEquals("sign in id", friend.getOwner());
            Assert.assertEquals("user id", friend.getUser());
            Assert.assertNull(friend.getMemo());
            Assert.assertEquals(0, friend.getState());
            Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
            friend = pl.getList().get(1);
            Assert.assertEquals("user id", friend.getOwner());
            Assert.assertEquals("sign in id", friend.getUser());
            Assert.assertEquals("memo", friend.getMemo());
            Assert.assertEquals(1, friend.getState());
            Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
        }

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user id\"}}");
        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"sign in id\":{\"id\":\"sign in id\",\"name\":\"sign in name\"}}}");
        for (int i = 0; i < 5; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("user", "sign in id");
            mockHelper.getRequest().addParameter("memo", "friend memo");
            mockHelper.mock("/friend/create");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            Assert.assertEquals("", object.getString("data"));
            PageList<FriendModel> pl = liteOrm.query(new LiteQuery(FriendModel.class).order("c_user desc"), null);
            Assert.assertEquals(2, pl.getList().size());
            FriendModel friend = pl.getList().get(0);
            Assert.assertEquals("sign in id", friend.getOwner());
            Assert.assertEquals("user id", friend.getUser());
            Assert.assertEquals("friend memo", friend.getMemo());
            Assert.assertEquals(2, friend.getState());
            Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
            friend = pl.getList().get(1);
            Assert.assertEquals("user id", friend.getOwner());
            Assert.assertEquals("sign in id", friend.getUser());
            Assert.assertEquals("memo", friend.getMemo());
            Assert.assertEquals(2, friend.getState());
            Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
        }

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"new user id\":{\"id\":\"new user id\",\"name\":\"new user name\"}}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "new user id");
        mockHelper.getRequest().addParameter("memo", "new memo");
        mockHelper.mock("/friend/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        PageList<FriendModel> pl = liteOrm.query(new LiteQuery(FriendModel.class).where("c_state<?").order("c_state"), new Object[]{2});
        Assert.assertEquals(2, pl.getList().size());
        FriendModel friend = pl.getList().get(0);
        Assert.assertEquals("sign in id", friend.getOwner());
        Assert.assertEquals("new user id", friend.getUser());
        Assert.assertNull(friend.getMemo());
        Assert.assertEquals(0, friend.getState());
        Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
        friend = pl.getList().get(1);
        Assert.assertEquals("new user id", friend.getOwner());
        Assert.assertEquals("sign in id", friend.getUser());
        Assert.assertEquals("new memo", friend.getMemo());
        Assert.assertEquals(1, friend.getState());
        Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
    }
}
