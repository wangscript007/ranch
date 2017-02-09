package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class PasswordTest extends TestSupport {
    @Test
    public void password() {
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
    }
}
