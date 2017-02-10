package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.ctrl.validate.Validators;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class StateTest extends TestSupport {
    @Inject
    private Cache cache;

    @Test
    public void state() {
        UserModel user1 = create(1, 0);
        UserModel user2 = create(2, 0);

        mockHelper.reset();
        mockHelper.mock("/user/state");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1522, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(UserModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/user/state");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1522, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(UserModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.getRequest().addParameter("state", "2");
        mockHelper.mock("/user/state");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1524, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(UserModel.NAME + ".state"), 0, 1), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.getRequest().addParameter("state", "1");
        mockHelper.mock("/user/state");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.getRequest().addParameter("state", "1");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/user/state");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1525, object.getInt("code"));
        Assert.assertEquals(message.get(UserModel.NAME + ".not-exists"), object.getString("message"));

        cache.put(UserModel.NAME + ".service.model:" + user1.getId(), user1, false);
        cache.put(UserModel.NAME + ".service.model:" + user2.getId(), user2, false);
        cache.put(UserModel.NAME + ".service.json:" + user1.getId(), new JSONObject(), false);
        cache.put(UserModel.NAME + ".service.json:" + user2.getId(), new JSONObject(), false);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", user1.getId());
        mockHelper.getRequest().addParameter("state", "1");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/user/state");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        UserModel user11 = liteOrm.findById(UserModel.class, user1.getId());
        Assert.assertEquals(1, user11.getState());
        UserModel user22 = liteOrm.findById(UserModel.class, user2.getId());
        Assert.assertEquals(0, user22.getState());
        Assert.assertNull(cache.get(UserModel.NAME + ".service.model:" + user1.getId()));
        Assert.assertNull(cache.get(UserModel.NAME + ".service.json:" + user1.getId()));
        Assert.assertNotNull(cache.get(UserModel.NAME + ".service.model:" + user2.getId()));
        Assert.assertNotNull(cache.get(UserModel.NAME + ".service.json:" + user2.getId()));
    }
}
