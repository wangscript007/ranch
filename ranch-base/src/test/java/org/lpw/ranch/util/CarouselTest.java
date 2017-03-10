package org.lpw.ranch.util;

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
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
public class CarouselTest extends TephraTestSupport {
    @Inject
    private Thread thread;
    @Inject
    private MockHelper mockHelper;
    @Inject
    private MockCarousel mockCarousel;
    @Inject
    private Carousel carousel;

    @Test
    public void get() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        JSONObject object = carousel.get("key", "id 1");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id 1", object.getString("id"));

        mockCarousel.register("key", "{\"code\":1}");
        object = carousel.get("key", "id 1");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id 1", object.getString("id"));

        mockCarousel.register("key", "{\"code\":0,\"data\":{\"name\":\"carousel\"}}");
        object = carousel.get("key", "id 2");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id 2", object.getString("id"));

        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 3\":{\"name\":\"carousel\"}}}");
        object = carousel.get("key", "id 3");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("carousel", object.getString("name"));

        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 4\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        object = carousel.get("key", "id 4");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("new id", object.getString("id"));
        Assert.assertEquals("carousel", object.getString("name"));
    }

    @Test
    public void service() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.getHeader().setMap(newMap("header"));
        mockHelper.mock("/carousel");
        object();
        array();
    }

    private void object() {
        mockCarousel.reset();
        mockCarousel.register("key.object", (key, header, parameter, cacheTime) -> {
            JSONObject obj = new JSONObject();
            if (cacheTime < 5) {
                JSONObject data = new JSONObject();
                if (header != null)
                    data.putAll(header);
                if (parameter != null)
                    data.putAll(parameter);
                data.put("cacheTime", cacheTime);
                obj.put("code", 0);
                obj.put("data", data);
            } else
                obj.put("code", cacheTime);

            return obj.toString();
        });

        Map<String, String> parameter = newMap("parameter");
        JSONObject object = carousel.service("key", null, parameter, false, JSONObject.class);
        Assert.assertTrue(object.isEmpty());

        object = carousel.service("key.object", null, parameter, false, JSONObject.class);
        Assert.assertEquals(5, object.size());
        equals(object, "header");
        equals(object, "parameter");
        Assert.assertEquals(0, object.getIntValue("cacheTime"));

        object = carousel.service("key.object", null, parameter, 1, JSONObject.class);
        Assert.assertEquals(5, object.size());
        equals(object, "header");
        equals(object, "parameter");
        Assert.assertEquals(1, object.getIntValue("cacheTime"));

        Map<String, String> header = newMap("new header");
        object = carousel.service("key.object", header, parameter, 2, JSONObject.class);
        Assert.assertEquals(5, object.size());
        equals(object, "new header");
        equals(object, "parameter");
        Assert.assertEquals(2, object.getIntValue("cacheTime"));

        object = carousel.service("key.object", header, parameter, 5, JSONObject.class);
        Assert.assertTrue(object.isEmpty());
        object = carousel.service("key.object", header, parameter, true, JSONObject.class);
        Assert.assertTrue(object.isEmpty());
    }

    private void array() {
        mockCarousel.reset();
        mockCarousel.register("key.array", (key, header, parameter, cacheTime) -> {
            JSONObject obj = new JSONObject();
            if (cacheTime < 5) {
                JSONObject data = new JSONObject();
                if (header != null)
                    data.putAll(header);
                if (parameter != null)
                    data.putAll(parameter);
                data.put("cacheTime", cacheTime);
                obj.put("code", 0);
                JSONArray array = new JSONArray();
                array.add(data);
                obj.put("data", array);
            } else
                obj.put("code", cacheTime);

            return obj.toString();
        });

        Map<String, String> parameter = newMap("parameter");
        JSONArray array = carousel.service("key", null, parameter, false, JSONArray.class);
        Assert.assertTrue(array.isEmpty());

        array = carousel.service("key.array", null, parameter, false, JSONArray.class);
        Assert.assertEquals(1, array.size());
        JSONObject object = array.getJSONObject(0);
        Assert.assertEquals(5, object.size());
        equals(object, "header");
        equals(object, "parameter");
        Assert.assertEquals(0, object.getIntValue("cacheTime"));

        array = carousel.service("key.array", null, parameter, 1, JSONArray.class);
        Assert.assertEquals(1, array.size());
        object = array.getJSONObject(0);
        Assert.assertEquals(5, object.size());
        equals(object, "header");
        equals(object, "parameter");
        Assert.assertEquals(1, object.getIntValue("cacheTime"));

        Map<String, String> header = newMap("new header");
        array = carousel.service("key.array", header, parameter, 2, JSONArray.class);
        Assert.assertEquals(1, array.size());
        object = array.getJSONObject(0);
        Assert.assertEquals(5, object.size());
        equals(object, "new header");
        equals(object, "parameter");
        Assert.assertEquals(2, object.getIntValue("cacheTime"));

        array = carousel.service("key.array", header, parameter, 5, JSONArray.class);
        Assert.assertTrue(array.isEmpty());
        array = carousel.service("key.array", header, parameter, true, JSONArray.class);
        Assert.assertTrue(array.isEmpty());
    }

    private Map<String, String> newMap(String name) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 2; i++)
            map.put(name + " " + i, name + " value " + i);

        return map;
    }

    private void equals(JSONObject object, String name) {
        for (int i = 0; i < 2; i++)
            Assert.assertEquals(name + " value " + i, object.getString(name + " " + i));
    }
}
