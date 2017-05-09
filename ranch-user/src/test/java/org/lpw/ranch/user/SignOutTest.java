package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.user.auth.AuthModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class SignOutTest extends TestSupport {
    @Test
    public void signOut() {
        List<UserModel> list = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            list.add(create(i));

        mockHelper.reset();
        mockHelper.getSession().setId("sign out session id");
        mockHelper.mock("/user/sign-out");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertNull(session.get(UserModel.NAME + ".service.session"));

        list.forEach(user -> {
            createAuth(user.getId(), "sign out session id", 0);
            mockHelper.reset();
            mockHelper.getSession().setId("sign out session id");
            session.set(UserModel.NAME + ".service.session", user);
            mockHelper.mock("/user/sign-out");
            JSONObject obj = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, obj.getIntValue("code"));
            Assert.assertEquals("", obj.getString("data"));
            Assert.assertNull(session.get(UserModel.NAME + ".service.session"));
            Assert.assertNull(find("sign out session id"));
        });

        AuthModel auth = createAuth(list.get(0).getId(), "sign out session id", 1);
        auth.setTime(dateTime.now());
        liteOrm.save(auth);
        mockHelper.reset();
        mockHelper.getSession().setId("sign out session id");
        session.set(UserModel.NAME + ".service.session", list.get(0));
        mockHelper.mock("/user/sign-out");
        JSONObject obj = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, obj.getIntValue("code"));
        Assert.assertEquals("", obj.getString("data"));
        Assert.assertNull(session.get(UserModel.NAME + ".service.session"));
        auth(list.get(0).getId(), "sign out session id", 1, new long[]{0, 2000});
    }
}
