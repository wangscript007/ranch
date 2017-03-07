package org.lpw.ranch.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.util.TimeUnit;

import java.sql.Timestamp;

/**
 * @author lpw
 */
public class NewestTest extends TestSupport {
    @Test
    public void newest() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/message/newest");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.mock("/message/newest");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(2, data.size());
        Assert.assertTrue(System.currentTimeMillis() - data.getLongValue("time") < 1000L);
        Assert.assertTrue(data.getJSONArray("messages").isEmpty());

        mockCarousel.register("ranch.group.query-by-user", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONArray array = new JSONArray();
            for (int i = 0; i < 10; i++) {
                JSONObject group = new JSONObject();
                group.put("id", "group " + i);
                array.add(group);
            }
            json.put("data", array);

            return json.toJSONString();
        });
        long time = System.currentTimeMillis() - TimeUnit.Hour.getTime();
        for (int i = 0; i < 10; i++) {
            MessageModel message = new MessageModel();
            message.setSender("sign in id");
            message.setType(i % 2);
            message.setReceiver((i % 2 == 0 ? "user " : "group ") + i);
            message.setFormat(i);
            message.setContent("content " + i);
            message.setTime(new Timestamp(time + i * TimeUnit.Minute.getTime()));
            liteOrm.save(message);
        }
        for (int i = 10; i < 20; i++) {
            MessageModel message = new MessageModel();
            message.setSender("user " + i % 10);
            message.setType(i % 2);
            message.setReceiver(i % 2 == 0 ? "sign in id" : ("group " + i % 10));
            message.setFormat(i);
            message.setContent("content " + i);
            message.setTime(new Timestamp(time + i * TimeUnit.Minute.getTime()));
            liteOrm.save(message);
        }
        for (int i = 20; i < 30; i++) {
            MessageModel message = new MessageModel();
            message.setSender("user id " + i % 2);
            message.setType(i % 2);
            message.setReceiver(i % 2 == 0 ? "new user id" : ("group " + i));
            message.setFormat(i);
            message.setContent("content " + i);
            message.setTime(new Timestamp(time + i * TimeUnit.Minute.getTime()));
            liteOrm.save(message);
        }

        mockHelper.reset();
        mockHelper.mock("/message/newest");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(2, data.size());
        Assert.assertTrue(System.currentTimeMillis() - data.getLongValue("time") < 1000L);
        JSONArray messages = data.getJSONArray("messages");
        Assert.assertEquals(10, messages.size());
        for (int i = 0; i < 10; i += 2)
            group(messages.getJSONObject(i), 9 - i, time, 2);
        for (int i = 1; i < 10; i += 2)
            user(messages.getJSONObject(i), 9 - i, time, 2);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("time", "" + (time + 10 * TimeUnit.Minute.getTime()));
        mockHelper.mock("/message/newest");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(2, data.size());
        Assert.assertTrue(System.currentTimeMillis() - data.getLongValue("time") < 1000L);
        messages = data.getJSONArray("messages");
        Assert.assertEquals(10, messages.size());
        for (int i = 0; i < 10; i += 2)
            group(messages.getJSONObject(i), 9 - i, time, 1);
        for (int i = 1; i < 10; i += 2)
            user(messages.getJSONObject(i), 9 - i, time, 1);
    }

    private void group(JSONObject object, int i, long time, int size) {
        Assert.assertEquals(3, object.size());
        Assert.assertEquals("group " + i, object.getString("id"));
        Assert.assertTrue(object.getBooleanValue("group"));
        JSONArray list = object.getJSONArray("list");
        Assert.assertEquals(size, list.size());
        JSONObject message = list.getJSONObject(0);
        Assert.assertEquals("user " + i, message.getString("sender"));
        Assert.assertEquals(1, message.getIntValue("type"));
        Assert.assertEquals("group " + i, message.getString("receiver"));
        Assert.assertEquals(10 + i, message.getIntValue("format"));
        Assert.assertEquals("content " + (10 + i), message.getString("content"));
        Assert.assertEquals(converter.toString(new Timestamp(time + (10 + i) * TimeUnit.Minute.getTime())), message.getString("time"));
        if (size > 1) {
            message = list.getJSONObject(1);
            Assert.assertEquals("sign in id", message.getString("sender"));
            Assert.assertEquals(1, message.getIntValue("type"));
            Assert.assertEquals("group " + i, message.getString("receiver"));
            Assert.assertEquals(i, message.getIntValue("format"));
            Assert.assertEquals("content " + i, message.getString("content"));
            Assert.assertEquals(converter.toString(new Timestamp(time + i * TimeUnit.Minute.getTime())), message.getString("time"));
        }
    }

    private void user(JSONObject object, int i, long time, int size) {
        Assert.assertEquals(3, object.size());
        Assert.assertEquals("user " + i, object.getString("id"));
        Assert.assertFalse(object.getBooleanValue("group"));
        JSONArray list = object.getJSONArray("list");
        Assert.assertEquals(size, list.size());
        JSONObject message = list.getJSONObject(0);
        Assert.assertEquals("user " + i, message.getString("sender"));
        Assert.assertEquals(0, message.getIntValue("type"));
        Assert.assertEquals("sign in id", message.getString("receiver"));
        Assert.assertEquals(10 + i, message.getIntValue("format"));
        Assert.assertEquals("content " + (10 + i), message.getString("content"));
        Assert.assertEquals(converter.toString(new Timestamp(time + (10 + i) * TimeUnit.Minute.getTime())), message.getString("time"));
        if (size > 1) {
            message = list.getJSONObject(1);
            Assert.assertEquals("sign in id", message.getString("sender"));
            Assert.assertEquals(0, message.getIntValue("type"));
            Assert.assertEquals("user " + i, message.getString("receiver"));
            Assert.assertEquals(i, message.getIntValue("format"));
            Assert.assertEquals("content " + i, message.getString("content"));
            Assert.assertEquals(converter.toString(new Timestamp(time + i * TimeUnit.Minute.getTime())), message.getString("time"));
        }
    }
}
