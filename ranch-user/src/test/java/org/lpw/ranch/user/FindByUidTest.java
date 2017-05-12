package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.ctrl.validate.Validators;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class FindByUidTest extends TestSupport {
    @Inject
    private Cache cache;

    @Test
    public void findByUid() {
        List<UserModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserModel user = create(i);
            createAuth(user.getId(), "uid " + i, 1);
            list.add(user);
        }

        mockHelper.reset();
        mockHelper.mock("/user/find-by-uid");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1527, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(UserModel.NAME + ".uid")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid");
        mockHelper.mock("/user/find-by-uid");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/user/find-by-uid");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1528, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.auth.uid.not-exists", message.get(UserModel.NAME + ".uid")), object.getString("message"));

        for (int i = 0; i < 2; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("uid", "uid 0");
            request.putSign(mockHelper.getRequest().getMap());
            mockHelper.mock("/user/find-by-uid");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            equals(list.get(0), object.getJSONObject("data"));

            set(liteOrm.findById(UserModel.class, list.get(0).getId()), 5, 15);
        }

        cache.remove(UserModel.NAME + ".service.json:" + list.get(0).getId());
        cache.remove(UserModel.NAME + ".service.json:uid 0");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid 0");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/user/find-by-uid");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(list.get(0), object.getJSONObject("data"));

        createAuth("user 11", "uid 11", 1);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid 11");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/user/find-by-uid");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());
    }
}
