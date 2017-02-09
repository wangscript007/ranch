package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.test.GeneratorAspect;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class SignUpTest extends TestSupport {
    @Inject
    private GeneratorAspect generatorAspect;

    @Test
    public void signUp() {
        mockHelper.reset();
        mockHelper.mock("/user/sign-up");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1501, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(UserModel.NAME + ".uid")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", generator.random(101));
        mockHelper.mock("/user/sign-up");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1502, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(UserModel.NAME + ".uid"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "sign up uid 1");
        mockHelper.getRequest().addParameter("type", "1");
        mockHelper.mock("/user/sign-up");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1503, object.getInt("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".password.empty"), object.getString("message"));

        List<String> list = new ArrayList<>();
        list.add("mock session id");
        list.add("code 0");
        list.add("mock session id");
        list.add("mock session id");
        list.add("code 0");
        list.add("code 1");
        generatorAspect.randomString(list);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "sign up uid 1");
        mockHelper.getRequest().addParameter("password", "password 1");
        mockHelper.mock("/user/sign-up");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        equalsSignUp(object.getJSONObject("data"), "sign up uid 1", 0, null, "code 0");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "sign up uid 1");
        mockHelper.getRequest().addParameter("password", "password 1");
        mockHelper.mock("/user/sign-up");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1504, object.getInt("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".sign-up.failure"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "sign up uid 2");
        mockHelper.getRequest().addParameter("password", "password 2");
        mockHelper.getRequest().addParameter("type", "1");
        mockHelper.mock("/user/sign-up");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        equalsSignUp(object.getJSONObject("data"), "sign up uid 2", 1, "password 2", "code 1");

        generatorAspect.randomString(null);
    }
}
