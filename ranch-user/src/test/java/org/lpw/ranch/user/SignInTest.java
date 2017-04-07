package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.MockWeixin;
import org.lpw.tephra.util.Thread;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.util.List;

/**
 * @author lpw
 */
public class SignInTest extends TestSupport {
    @Inject
    private Thread thread;
    @Inject
    private MockWeixin mockWeixin;

    @Test
    public void signIn() {
        UserModel user1 = create(1, 0);
        UserModel user2 = create(2, 0);
        UserModel user3 = create(3, 1);
        AuthModel auth0 = createAuth(user1.getId(), "mac id 1", 0);
        createAuth(user1.getId(), "uid 1", 1);
        createAuth("user 2", "uid 2", 2);
        createAuth(user2.getId(), "uid 3", 1);
        createAuth(user3.getId(), "uid 4", 0);

        mockHelper.reset();
        mockHelper.mock("/user/sign-in");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1501, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(UserModel.NAME + ".uid")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid");
        mockHelper.getRequest().addParameter("type", "1");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1503, object.getIntValue("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".password.empty"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid");
        mockHelper.getRequest().addParameter("macId", generator.random(101));
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1505, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(UserModel.NAME + ".macId"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "sign up uid 1");
        mockHelper.getRequest().addParameter("type", "-1");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1527, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(UserModel.NAME + ".type"), 0, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "sign up uid 1");
        mockHelper.getRequest().addParameter("type", "3");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1527, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(UserModel.NAME + ".type"), 0, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid");
        mockHelper.getRequest().addParameter("macId", "mac id");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1506, object.getIntValue("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".sign-in.failure"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid 1");
        mockHelper.getRequest().addParameter("macId", "mac id");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1506, object.getIntValue("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".sign-in.failure"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid 2");
        mockHelper.getRequest().addParameter("macId", "mac id");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1506, object.getIntValue("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".sign-in.failure"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid 1");
        mockHelper.getRequest().addParameter("password", "password");
        mockHelper.getRequest().addParameter("macId", "mac id");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1506, object.getIntValue("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".sign-in.failure"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid 4");
        mockHelper.getRequest().addParameter("macId", "mac id");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1506, object.getIntValue("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".sign-in.failure"), object.getString("message"));

        mockWeixin.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid 4");
        mockHelper.getRequest().addParameter("password", "password 4");
        mockHelper.getRequest().addParameter("type", "2");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1506, object.getIntValue("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".sign-in.failure"), object.getString("message"));
        List<Object[]> args = mockWeixin.getArgs();
        Assert.assertEquals(1, args.size());
        Assert.assertEquals(2, args.get(0).length);
        Assert.assertEquals("password 4", args.get(0)[0]);
        Assert.assertEquals("uid 4", args.get(0)[1]);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "mac id 1");
        mockHelper.getRequest().addParameter("macId", "mac id");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(user1, object.getJSONObject("data"));

        for (int i = 0; i < 2; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("uid", "uid 1");
            mockHelper.getRequest().addParameter("password", "password 1");
            mockHelper.getRequest().addParameter("macId", "mac id");
            mockHelper.getRequest().addParameter("type", "1");
            mockHelper.mock("/user/sign-in");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            equals(user1, object.getJSONObject("data"));
            AuthModel auth = liteOrm.findById(AuthModel.class, auth0.getId());
            Assert.assertEquals(user1.getId(), auth.getUser());
            Assert.assertEquals("mac id 1", auth.getUid());
            auth = liteOrm.findOne(new LiteQuery(AuthModel.class).where("c_uid=?"), new Object[]{"mac id"});
            Assert.assertEquals(user1.getId(), auth.getUser());

            for (int j = 0; j < 2; j++) {
                mockHelper.reset();
                mockHelper.getRequest().addParameter("uid", "uid 3");
                mockHelper.getRequest().addParameter("password", "password 2");
                mockHelper.getRequest().addParameter("macId", "mac id");
                mockHelper.getRequest().addParameter("type", "1");
                mockHelper.mock("/user/sign-in");
                object = mockHelper.getResponse().asJson();
                Assert.assertEquals(0, object.getIntValue("code"));
                equals(user2, object.getJSONObject("data"));
                auth = liteOrm.findOne(new LiteQuery(AuthModel.class).where("c_uid=?"), new Object[]{"mac id"});
                Assert.assertEquals(user2.getId(), auth.getUser());
            }
        }

        int count = liteOrm.count(new LiteQuery(AuthModel.class), null);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid 1");
        mockHelper.getRequest().addParameter("password", "password 1");
        mockHelper.getRequest().addParameter("type", "1");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(user1, object.getJSONObject("data"));
        Assert.assertEquals(count, liteOrm.count(new LiteQuery(AuthModel.class), null));

        signInByWeixin();
    }

    private void signInByWeixin() {
        long time = 0L;
        String code = null;
        for (int i = 0; i < 2; i++) {
            mockWeixin.reset();
            mockWeixin.auth("weixin open id", "weixin nickname " + i, "weixin portrait " + i);
            mockHelper.reset();
            mockHelper.getRequest().addParameter("uid", "uid 5");
            mockHelper.getRequest().addParameter("password", "password 5");
            mockHelper.getRequest().addParameter("type", "2");
            mockHelper.mock("/user/sign-in");
            JSONObject object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));

            AuthModel auth = liteOrm.findOne(new LiteQuery(AuthModel.class).where("c_uid=?"), new Object[]{"weixin open id"});
            Assert.assertEquals(2, auth.getType());
            JSONObject data = object.getJSONObject("data");
            Assert.assertEquals(auth.getUser(), data.getString("id"));
            for (String name : new String[]{"password", "name", "mobile", "email", "address", "birthday"})
                Assert.assertFalse(data.containsKey(name));
            for (String name : new String[]{"gender", "grade", "state"})
                Assert.assertEquals(0, data.getIntValue(name));
            Assert.assertEquals("weixin nickname 0", data.getString("nick"));
            Assert.assertEquals("weixin portrait 0", data.getString("portrait"));
            if (i == 0) {
                Assert.assertTrue(System.currentTimeMillis() - dateTime.toDate(data.getString("register")).getTime() < 2000L);
                Assert.assertEquals(8, data.getString("code").length());
            } else {
                Assert.assertTrue(System.currentTimeMillis() - dateTime.toDate(data.getString("register")).getTime() > 2000L);
                Assert.assertTrue(time - dateTime.toDate(data.getString("register")).getTime() < 2000L);
                Assert.assertEquals(code, data.getString("code"));
            }

            UserModel user = liteOrm.findById(UserModel.class, auth.getUser());
            Assert.assertNull(user.getPassword());
            Assert.assertNull(user.getName());
            Assert.assertEquals("weixin nickname 0", user.getNick());
            Assert.assertNull(user.getMobile());
            Assert.assertNull(user.getEmail());
            Assert.assertEquals("weixin portrait 0", user.getPortrait());
            Assert.assertNull(user.getAddress());
            Assert.assertNull(user.getBirthday());
            Assert.assertEquals(0, user.getGender());
            Assert.assertEquals(0, user.getGrade());
            Assert.assertEquals(0, user.getState());
            if (i == 0) {
                Assert.assertTrue(System.currentTimeMillis() - user.getRegister().getTime() < 2000L);
                Assert.assertEquals(8, user.getCode().length());
            } else {
                Assert.assertTrue(System.currentTimeMillis() - user.getRegister().getTime() > 2000L);
                Assert.assertTrue(time - user.getRegister().getTime() < 2000L);
                Assert.assertEquals(code, user.getCode());
            }
            time = user.getRegister().getTime();
            code = user.getCode();

            List<Object[]> args = mockWeixin.getArgs();
            Assert.assertEquals(i == 0 ? 3 : 1, args.size());
            Assert.assertEquals(2, args.get(0).length);
            Assert.assertEquals("password 5", args.get(0)[0]);
            Assert.assertEquals("uid 5", args.get(0)[1]);
            if (i == 0) {
                Assert.assertEquals(0, args.get(1).length);
                Assert.assertEquals(0, args.get(2).length);
            }
            thread.sleep(3, TimeUnit.Second);
        }
    }
}
