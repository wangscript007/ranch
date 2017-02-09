package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class ModifyTest extends TestSupport {
    @Test
    public void modify() {
        mockHelper.reset();
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/user/modify");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1507, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(UserModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("nick", generator.random(101));
        mockHelper.mock("/user/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1508, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(UserModel.NAME + ".nick"), 100), object.getString("message"));
    }
}
