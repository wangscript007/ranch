package org.lpw.ranch.last;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.TimeUnit;

import java.sql.Timestamp;

/**
 * @author lpw
 */
public class SaveTest extends TestSupport {
    @Test
    public void save() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/last/save");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2001, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LastModel.NAME + ".type")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", generator.random(101));
        mockHelper.mock("/last/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2002, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(LastModel.NAME + ".type"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.mock("/last/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.mock("/last/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals("sign in id", data.getString("user"));
        Assert.assertEquals("type value", data.getString("type"));
        long time = data.getLongValue("millisecond");
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        Assert.assertEquals(dateTime.toString(new Timestamp(time)), data.getString("time"));

        thread.sleep(3, TimeUnit.Second);
        String id = data.getString("id");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.mock("/last/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(id, data.getString("id"));
        Assert.assertEquals("sign in id", data.getString("user"));
        Assert.assertEquals("type value", data.getString("type"));
        time = data.getLongValue("millisecond");
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        Assert.assertEquals(dateTime.toString(new Timestamp(time)), data.getString("time"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "new type value");
        mockHelper.mock("/last/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertNotEquals(id, data.getString("id"));
        Assert.assertEquals("sign in id", data.getString("user"));
        Assert.assertEquals("new type value", data.getString("type"));
        time = data.getLongValue("millisecond");
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        Assert.assertEquals(dateTime.toString(new Timestamp(time)), data.getString("time"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"new sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.mock("/last/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertNotEquals(id, data.getString("id"));
        Assert.assertEquals("new sign in id", data.getString("user"));
        Assert.assertEquals("type value", data.getString("type"));
        time = data.getLongValue("millisecond");
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        Assert.assertEquals(dateTime.toString(new Timestamp(time)), data.getString("time"));
    }
}
