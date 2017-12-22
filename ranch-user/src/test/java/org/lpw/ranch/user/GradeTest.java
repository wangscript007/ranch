package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.ctrl.validate.Validators;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class GradeTest extends TestSupport {
    @Inject
    private Cache cache;

    @Test
    public void grade() {
        UserModel user1 = create(1);
        UserModel user2 = create(2);

        mockHelper.reset();
        mockHelper.mock("/user/grade");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1522, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(UserModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/user/grade");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1522, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(UserModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.getRequest().addParameter("grade", "100");
        mockHelper.mock("/user/grade");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1523, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(UserModel.NAME + ".grade"), 0, 99), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.getRequest().addParameter("grade", "11");
        mockHelper.mock("/user/grade");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.getRequest().addParameter("grade", "11");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/user/grade");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1525, object.getIntValue("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".not-exists"), object.getString("message"));

        cache.put(UserModel.NAME + ".service.model:" + user1.getId(), user1, false);
        cache.put(UserModel.NAME + ".service.model:" + user2.getId(), user2, false);
        cache.put(UserModel.NAME + ".service.json:" + user1.getId(), new JSONObject(), false);
        cache.put(UserModel.NAME + ".service.json:" + user2.getId(), new JSONObject(), false);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", user1.getId());
        mockHelper.getRequest().addParameter("grade", "11");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/user/grade");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        UserModel user11 = liteOrm.findById(UserModel.class, user1.getId());
        Assert.assertEquals(11, user11.getGrade());
        UserModel user22 = liteOrm.findById(UserModel.class, user2.getId());
        Assert.assertEquals(2, user22.getGrade());
        Assert.assertNull(cache.get(UserModel.NAME + ".service.model:" + user1.getId()));
        Assert.assertNull(cache.get(UserModel.NAME + ".service.json:" + user1.getId()));
        Assert.assertNotNull(cache.get(UserModel.NAME + ".service.model:" + user2.getId()));
        Assert.assertNotNull(cache.get(UserModel.NAME + ".service.json:" + user2.getId()));
    }
}
