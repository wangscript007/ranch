package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.context.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class SignTest extends TestSupport {
    @Autowired
    private Session session;

    @Test
    public void sign() {
        List<UserModel> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(create(i));
        }

        mockHelper.reset();
        mockHelper.mock("/user/sign");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        list.forEach(user -> {
            mockHelper.reset();
            session.set(mockHelper.getSession().getId(), UserModel.NAME + ".service.session", user);
            mockHelper.mock("/user/sign");
            JSONObject obj = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, obj.getInt("code"));
            equals(user, obj.getJSONObject("data"));
        });
    }
}
