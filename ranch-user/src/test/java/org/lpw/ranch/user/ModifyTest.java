package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class ModifyTest extends TestSupport {
    @Inject
    private Session session;

    @Test
    public void modify() {
        UserModel user1 = create(1, 0);
        UserModel user2 = create(2, 0);
        createAuth(user1.getId(), "uid 1", 0);
        createAuth(user2.getId(), "uid 2", 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("idcard", generator.random(101));
        mockHelper.mock("/user/modify");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1507, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(UserModel.NAME + ".idcard"), 100), object.getString("message"));


        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/user/modify");
         object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1508, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(UserModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("nick", generator.random(101));
        mockHelper.mock("/user/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1509, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(UserModel.NAME + ".nick"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("mobile", "mobile");
        mockHelper.mock("/user/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1510, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-mobile", message.get(UserModel.NAME + ".mobile")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("email", "email");
        mockHelper.mock("/user/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1511, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-email", message.get(UserModel.NAME + ".email")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("email", generator.random(50) + "@" + generator.random(46) + ".com");
        mockHelper.mock("/user/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1512, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(UserModel.NAME + ".email"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("gender", "3");
        mockHelper.mock("/user/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1513, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(UserModel.NAME + ".gender"), 0, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.mock("/user/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".need-sign-in"), object.getString("message"));

        mockHelper.reset();
        session.set(UserModel.NAME + ".service.session", user1);
        mockHelper.mock("/user/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(user1, object.getJSONObject("data"));

        mockHelper.reset();
        session.set(UserModel.NAME + ".service.session", user1);
        mockHelper.getRequest().addParameter("password", "password");
        mockHelper.getRequest().addParameter("secret", "secret");
        mockHelper.getRequest().addParameter("idcard", "idcard");
        mockHelper.getRequest().addParameter("name", "name");
        mockHelper.getRequest().addParameter("nick", "nick");
        mockHelper.getRequest().addParameter("mobile", "12312345678");
        mockHelper.getRequest().addParameter("email", "e@mail");
        mockHelper.getRequest().addParameter("portrait", "portrait");
        mockHelper.getRequest().addParameter("gender", "2");
        mockHelper.getRequest().addParameter("birthday", "2017-01-02");
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("register", "2017-01-02 03:04:05");
        mockHelper.getRequest().addParameter("grade", "2");
        mockHelper.getRequest().addParameter("state", "3");
        mockHelper.mock("/user/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        UserModel user11 = liteOrm.findById(UserModel.class, user1.getId());
        Assert.assertEquals(digest.md5(UserModel.NAME + digest.sha1("password 1" + UserModel.NAME)), user11.getPassword());
        Assert.assertEquals("name", user11.getName());
        Assert.assertEquals("nick", user11.getNick());
        Assert.assertEquals("12312345678", user11.getMobile());
        Assert.assertEquals("e@mail", user11.getEmail());
        Assert.assertEquals("portrait 1", user11.getPortrait());
        Assert.assertEquals(2, user11.getGender());
        Assert.assertEquals("2017-01-02", converter.toString(user11.getBirthday()));
        Assert.assertEquals("code 1", user11.getCode());
        Assert.assertTrue(System.currentTimeMillis() - user11.getRegister().getTime() < 2 * TimeUnit.Hour.getTime());
        Assert.assertEquals(1, user11.getGrade());
        Assert.assertEquals(0, user11.getState());
        equals(user11, object.getJSONObject("data"));
        equals(session.get(UserModel.NAME + ".service.session"), object.getJSONObject("data"));
    }
}
