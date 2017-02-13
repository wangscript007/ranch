package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteQuery;

/**
 * @author lpw
 */
public class SignInTest extends TestSupport {
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

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid 5");
        mockHelper.getRequest().addParameter("password", "password 5");
        mockHelper.getRequest().addParameter("type", "2");
        mockHelper.mock("/user/sign-in");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equalsSignUp(object.getJSONObject("data"), "uid 5", 2, null, null);
    }
}
