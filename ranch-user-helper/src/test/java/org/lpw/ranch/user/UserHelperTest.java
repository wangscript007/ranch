package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Thread;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.util.Calendar;

/**
 * @author lpw
 */
public class UserHelperTest extends TephraTestSupport {
    @Inject
    private Thread thread;
    @Inject
    private MockHelper mockHelper;
    @Inject
    private MockCarousel mockCarousel;
    @Inject
    private UserHelper userHelper;

    @Test
    public void get() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/user/get");
        mockCarousel.reset();
        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        JSONObject object = userHelper.get("id 1");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id 1", object.getString("id"));

        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        object = userHelper.get("id 1");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("new id", object.getString("id"));
        Assert.assertEquals("carousel", object.getString("name"));
    }

    @Test
    public void fill() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/user/get");
        mockCarousel.reset();
        mockCarousel.register("ranch.user.get", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data=new JSONObject();
            JSONObject user = new JSONObject();
            user.put("id", parameter.get("ids"));
            user.put("name", "name " + user.getString("id"));
            data.put(user.getString("id"),user);
            json.put("data", data);

            return json.toJSONString();
        });

        Assert.assertNull(userHelper.fill(null, null));

        JSONArray array = new JSONArray();
        Assert.assertTrue(userHelper.fill(array, null).isEmpty());
        Assert.assertTrue(array.isEmpty());

        String[] keys = new String[]{"user", "name", "author"};
        for (int i = 0; i < 10; i++) {
            JSONObject object = new JSONObject();
            for (String key : keys)
                object.put(key, key + " " + i);
            array.add(object);
        }
        Assert.assertEquals(array.hashCode(), userHelper.fill(array, null).hashCode());
        for (int i = 0; i < 10; i++) {
            JSONObject object = array.getJSONObject(i);
            for (String key : keys)
                Assert.assertEquals(key + " " + i, object.getString(key));
        }

        Assert.assertEquals(array.hashCode(), userHelper.fill(array, new String[0]).hashCode());
        for (int i = 0; i < 10; i++) {
            JSONObject object = array.getJSONObject(i);
            for (String key : keys)
                Assert.assertEquals(key + " " + i, object.getString(key));
        }

        array = userHelper.fill(array, new String[]{"user", "author"});
        for (int i = 0; i < 10; i++) {
            JSONObject object = array.getJSONObject(i);
            JSONObject obj = object.getJSONObject("user");
            Assert.assertEquals("user " + i, obj.getString("id"));
            Assert.assertEquals("name user " + i, obj.getString("name"));
            Assert.assertEquals("name " + i, object.getString("name"));
            obj = object.getJSONObject("author");
            Assert.assertEquals("author " + i, obj.getString("id"));
            Assert.assertEquals("name author " + i, obj.getString("name"));
        }
    }

    @Test
    public void sign() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        JSONObject object = userHelper.sign();
        Assert.assertTrue(object.isEmpty());

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"name\":\"sign\"}}");
        object = userHelper.sign();
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("sign", object.getString("name"));
    }
}
