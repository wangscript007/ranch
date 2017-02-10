package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.ctrl.validate.Validators;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class PasswordTest extends TestSupport {
    @Inject
    private Session session;

    @Test
    public void password() {
        UserModel user1 = create(1, 0);

        mockHelper.reset();
        mockHelper.mock("/user/password");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1514, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(UserModel.NAME + ".password.new")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("old", "password");
        mockHelper.getRequest().addParameter("new", "password");
        mockHelper.mock("/user/password");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1515, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "equals", message.get(UserModel.NAME + ".password.new"), message.get(UserModel.NAME + ".password.old")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("new", "password");
        mockHelper.getRequest().addParameter("repeat", "repeat");
        mockHelper.mock("/user/password");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1516, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-equals", message.get(UserModel.NAME + ".password.repeat"), message.get(UserModel.NAME + ".password.new")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("new", "new password");
        mockHelper.getRequest().addParameter("repeat", "new password");
        mockHelper.mock("/user/password");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getInt("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".need-sign-in"), object.getString("message"));

        mockHelper.reset();
        session.set(UserModel.NAME + ".service.session", user1);
        mockHelper.getRequest().addParameter("new", "new password");
        mockHelper.getRequest().addParameter("repeat", "new password");
        mockHelper.mock("/user/password");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1517, object.getInt("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".password.illegal"), object.getString("message"));

        mockHelper.reset();
        session.set(UserModel.NAME + ".service.session", user1);
        mockHelper.getRequest().addParameter("old", "old password");
        mockHelper.getRequest().addParameter("new", "new password");
        mockHelper.getRequest().addParameter("repeat", "new password");
        mockHelper.mock("/user/password");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1517, object.getInt("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".password.illegal"), object.getString("message"));

        mockHelper.reset();
        session.set(UserModel.NAME + ".service.session", user1);
        mockHelper.getRequest().addParameter("old", "password 1");
        mockHelper.getRequest().addParameter("new", "new password");
        mockHelper.getRequest().addParameter("repeat", "new password");
        mockHelper.mock("/user/password");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        UserModel user11 = liteOrm.findById(UserModel.class, user1.getId());
        Assert.assertEquals(digest.md5(UserModel.NAME + digest.sha1("new password" + UserModel.NAME)), user11.getPassword());

        user11.setPassword(null);
        liteOrm.save(user11);
        mockHelper.reset();
        session.set(UserModel.NAME + ".service.session", user11);
        mockHelper.getRequest().addParameter("new", "new password 11");
        mockHelper.getRequest().addParameter("repeat", "new password 11");
        mockHelper.mock("/user/password");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        UserModel user111 = liteOrm.findById(UserModel.class, user1.getId());
        Assert.assertEquals(digest.md5(UserModel.NAME + digest.sha1("new password 11" + UserModel.NAME)), user111.getPassword());
    }
}
