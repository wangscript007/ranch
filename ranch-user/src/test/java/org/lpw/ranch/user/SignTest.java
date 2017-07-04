package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.tephra.util.TimeUnit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class SignTest extends TestSupport {
    @Test
    public void sign() {
        List<UserModel> list = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            list.add(create(i));

        mockHelper.reset();
        mockHelper.getSession().setId("sign session id");
        mockHelper.mock("/user/sign");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        createAuth(list.get(0).getId(), "sign session id 1", 1);
        mockHelper.reset();
        mockHelper.getSession().setId("sign session id 1");
        mockHelper.mock("/user/sign");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        createAuth("user id 2", "sign session id 2", 0);
        mockHelper.reset();
        mockHelper.getSession().setId("sign session id 2");
        mockHelper.mock("/user/sign");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        AuthModel auth = createAuth(list.get(0).getId(), "sign session id", 0);
        auth.setTime(new Timestamp(System.currentTimeMillis() - 7 * TimeUnit.Day.getTime() - 2000));
        liteOrm.save(auth);
        mockHelper.reset();
        mockHelper.getSession().setId("sign session id");
        mockHelper.mock("/user/sign");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        auth.setTime(dateTime.now());
        liteOrm.save(auth);
        mockHelper.reset();
        mockHelper.getSession().setId("sign session id");
        online(list.get(0));
        mockHelper.mock("/user/sign");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(list.get(0), object.getJSONObject("data"));

        list.forEach(user -> {
            mockHelper.reset();
            mockHelper.getSession().setId("new sign session id");
            online(user);
            session.set(UserModel.NAME + ".service.session", user);
            mockHelper.mock("/user/sign");
            JSONObject obj = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, obj.getIntValue("code"));
            equals(user, obj.getJSONObject("data"));
            Assert.assertNull(find("new sign session id"));
        });
    }
}
