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
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(FriendModel.NAME + ".friend")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("friend", "friend");
        mockHelper.getRequest().addParameter("memo", generator.random(101));
        mockHelper.mock("/friend/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1603, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(FriendModel.NAME + ".memo"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("friend", "friend");
        mockHelper.getRequest().addParameter("memo", "memo");
        mockHelper.mock("/friend/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("friend", "friend");
        mockHelper.getRequest().addParameter("memo", "memo");
        mockHelper.mock("/friend/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1525, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.not-exists"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"friend id\":{\"id\":\"friend id\",\"name\":\"friend name\"}}}");
        for (int i = 0; i < 5; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("friend", "friend id");
            mockHelper.getRequest().addParameter("memo", "memo");
            mockHelper.mock("/friend/create");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            Assert.assertEquals("", object.getString("data"));
            PageList<FriendModel> pl = liteOrm.query(new LiteQuery(FriendModel.class).order("c_friend"), null);
            Assert.assertEquals(2, pl.getList().size());
            FriendModel friend = pl.getList().get(0);
            Assert.assertEquals("sign in id", friend.getOwner());
            Assert.assertEquals("friend id", friend.getFriend());
            Assert.assertNull(friend.getMemo());
            Assert.assertEquals(0, friend.getState());
            Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
            friend = pl.getList().get(1);
            Assert.assertEquals("friend id", friend.getOwner());
            Assert.assertEquals("sign in id", friend.getFriend());
            Assert.assertEquals("memo", friend.getMemo());
            Assert.assertEquals(1, friend.getState());
            Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
        }

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"friend id\"}}");
        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"sign in id\":{\"id\":\"sign in id\",\"name\":\"sign in name\"}}}");
        for (int i = 0; i < 5; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("friend", "sign in id");
            mockHelper.getRequest().addParameter("memo", "friend memo");
            mockHelper.mock("/friend/create");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            Assert.assertEquals("", object.getString("data"));
            PageList<FriendModel> pl = liteOrm.query(new LiteQuery(FriendModel.class).order("c_friend"), null);
            Assert.assertEquals(2, pl.getList().size());
            FriendModel friend = pl.getList().get(0);
            Assert.assertEquals("sign in id", friend.getOwner());
            Assert.assertEquals("friend id", friend.getFriend());
            Assert.assertEquals("friend memo", friend.getMemo());
            Assert.assertEquals(2, friend.getState());
            Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
            friend = pl.getList().get(1);
            Assert.assertEquals("friend id", friend.getOwner());
            Assert.assertEquals("sign in id", friend.getFriend());
            Assert.assertEquals("memo", friend.getMemo());
            Assert.assertEquals(2, friend.getState());
            Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
        }

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"new friend id\":{\"id\":\"new friend id\",\"name\":\"new friend name\"}}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("friend", "new friend id");
        mockHelper.getRequest().addParameter("memo", "new memo");
        mockHelper.mock("/friend/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        PageList<FriendModel> pl = liteOrm.query(new LiteQuery(FriendModel.class).where("c_state<?").order("c_state"), new Object[]{2});
        Assert.assertEquals(2, pl.getList().size());
        FriendModel friend = pl.getList().get(0);
        Assert.assertEquals("sign in id", friend.getOwner());
        Assert.assertEquals("new friend id", friend.getFriend());
        Assert.assertNull(friend.getMemo());
        Assert.assertEquals(0, friend.getState());
        Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
        friend = pl.getList().get(1);
        Assert.assertEquals("new friend id", friend.getOwner());
        Assert.assertEquals("sign in id", friend.getFriend());
        Assert.assertEquals("new memo", friend.getMemo());
        Assert.assertEquals(1, friend.getState());
        Assert.assertTrue(System.currentTimeMillis() - friend.getCreate().getTime() < 2000L);
    }
}
