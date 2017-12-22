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
public class FindByCodeTest extends TestSupport {
    @Inject
    private Cache cache;

    @Test
    public void findByCode() {
        List<UserModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list.add(create(i));

        mockHelper.reset();
        mockHelper.mock("/user/find-by-code");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1526, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(UserModel.NAME + ".code")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/user/find-by-code");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/user/find-by-code");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        for (int i = 0; i < 2; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("code", "code 0");
            sign.put(mockHelper.getRequest().getMap(), null);
            mockHelper.mock("/user/find-by-code");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            equals(list.get(0), object.getJSONObject("data"));

            set(liteOrm.findById(UserModel.class, list.get(0).getId()), 5, 15);
        }

        cache.remove(UserModel.NAME + ".service.json:" + list.get(0).getId());
        cache.remove(UserModel.NAME + ".service.json:code 0");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/user/find-by-code");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 5");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/user/find-by-code");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(liteOrm.findById(UserModel.class, list.get(0).getId()), object.getJSONObject("data"));
    }
}
