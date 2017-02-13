package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.context.Session;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class SignTest extends TestSupport {
    @Inject
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
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        list.forEach(user -> {
            mockHelper.reset();
            session.set(mockHelper.getSession().getId(), UserModel.NAME + ".service.session", user);
            mockHelper.mock("/user/sign");
            JSONObject obj = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, obj.getIntValue("code"));
            equals(user, obj.getJSONObject("data"));
        });
    }
}
