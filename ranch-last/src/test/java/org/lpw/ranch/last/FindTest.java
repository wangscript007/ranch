package org.lpw.ranch.last;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.TimeUnit;

import java.sql.Timestamp;

/**
 * @author lpw
 */
public class FindTest extends TestSupport {
    @Test
    public void find() {
        for (int i = 0; i < 5; i++)
            create(i);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/last/find");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2001, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LastModel.NAME + ".type")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", generator.random(101));
        mockHelper.mock("/last/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2002, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(LastModel.NAME + ".type"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.mock("/last/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 0");
        mockHelper.mock("/last/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 0\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 1");
        mockHelper.mock("/last/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user " + j + "\"}}");
                mockHelper.reset();
                mockHelper.getRequest().addParameter("type", "type " + j);
                mockHelper.mock("/last/find");
                object = mockHelper.getResponse().asJson();
                Assert.assertEquals(0, object.getIntValue("code"));
                equals(object.getJSONObject("data"), j);
            }
            liteOrm.delete(new LiteQuery(LastModel.class), null);
        }
    }

    private LastModel create(int i) {
        LastModel last = new LastModel();
        last.setUser("user " + i);
        last.setType("type " + i);
        last.setTime(new Timestamp(time - i * TimeUnit.Hour.getTime()));
        liteOrm.save(last);

        return last;
    }

    private void equals(JSONObject object, int i) {
        Assert.assertEquals("user " + i, object.getString("user"));
        Assert.assertEquals("type " + i, object.getString("type"));
        Assert.assertEquals(dateTime.toString(new Timestamp(time - i * TimeUnit.Hour.getTime())), object.getString("time"));
        Assert.assertTrue(Math.abs(time - i * TimeUnit.Hour.getTime() - object.getLongValue("time")) < 1000L);
        Assert.assertTrue(Math.abs(object.getLongValue("time") - time + i * TimeUnit.Hour.getTime()) < 1000L);
    }
}
