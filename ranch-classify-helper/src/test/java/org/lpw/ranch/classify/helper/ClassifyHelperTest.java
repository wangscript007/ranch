package org.lpw.ranch.classify.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.util.ServiceHelperTester;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Thread;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
public class ClassifyHelperTest extends TephraTestSupport {
    @Inject
    private Numeric numeric;
    @Inject
    private Thread thread;
    @Inject
    private MockHelper mockHelper;
    @Inject
    private MockCarousel mockCarousel;
    @Inject
    private ServiceHelperTester serviceHelperTester;
    @Inject
    private ClassifyHelper classifyHelper;

    @Test
    public void get() {
        serviceHelperTester.get((ClassifyHelperImpl) classifyHelper, "ranch.classify");
    }

    @Test
    public void find() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("ranch.classify.find", "{\"code\":0,\"data\":{\"id\":\"id value\"}}");
        JSONObject object = classifyHelper.find("code", "key");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id value", object.getString("id"));
    }

    @Test
    public void value() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        Map<String, String> map = new HashMap<>();
        map.put("value", "value from header");
        mockHelper.getHeader().setMap(map);
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("ranch.classify.find", "{\"code\":0,\"data\":{\"id\":\"id value\",\"value\":\"value 1\"}}");
        Assert.assertEquals("value 1", classifyHelper.value("code", "key"));
        mockCarousel.register("ranch.classify.find", "{\"code\":0,\"data\":{\"id\":\"id value\",\"value 1\":\"value 1\"}}");
        Assert.assertNull(classifyHelper.value("code", "key"));
    }

    @Test
    public void valueAsInt() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        Map<String, String> map = new HashMap<>();
        map.put("value", "value from header");
        mockHelper.getHeader().setMap(map);
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("ranch.classify.find", "{\"code\":0,\"data\":{\"id\":\"id value\",\"value\":\"1\"}}");
        Assert.assertEquals(1, classifyHelper.valueAsInt("code", "key", -1));
        mockCarousel.register("ranch.classify.find", "{\"code\":0,\"data\":{\"id\":\"id value\",\"value1\":\"1\"}}");
        Assert.assertEquals(-1, classifyHelper.valueAsInt("code", "key", -1));
    }

    @Test
    public void valueAsDouble() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        Map<String, String> map = new HashMap<>();
        map.put("value", "value from header");
        mockHelper.getHeader().setMap(map);
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("ranch.classify.find", "{\"code\":0,\"data\":{\"id\":\"id value\",\"value\":\"1.2\"}}");
        Assert.assertEquals(1.2D, classifyHelper.valueAsDouble("code", "key", -1), 0.001D);
        mockCarousel.register("ranch.classify.find", "{\"code\":0,\"data\":{\"id\":\"id value\",\"value1\":\"1.2\"}}");
        Assert.assertEquals(-1.0D, classifyHelper.valueAsInt("code", "key", -1), 0.001D);
    }

    @Test
    public void list() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        Map<String, String> map = new HashMap<>();
        map.put("header name", "header value");
        mockHelper.getHeader().setMap(map);
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("ranch.classify.list", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            if (header != null)
                data.putAll(header);
            if (parameter != null)
                data.putAll(parameter);
            data.put("cacheTime", cacheTime);
            JSONArray array = new JSONArray();
            array.add(data);
            json.put("data", array);

            return json.toJSONString();
        });

        JSONArray array = classifyHelper.list("code", "key", "name");
        Assert.assertEquals(1, array.size());
        JSONObject object = array.getJSONObject(0);
        Assert.assertEquals(5, object.size());
        Assert.assertEquals("header value", object.getString("header name"));
        Assert.assertEquals("code", object.getString("code"));
        Assert.assertEquals("key", object.getString("key"));
        Assert.assertEquals("name", object.getString("name"));
        Assert.assertEquals("5", object.getString("cacheTime"));
    }

    @Test
    public void fill() {
        serviceHelperTester.fill((ClassifyHelperImpl) classifyHelper, "ranch.classify");
    }

    @Test
    public void fillByKey() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        Map<String, String> map = new HashMap<>();
        map.put("header name", "header value");
        mockHelper.getHeader().setMap(map);
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("ranch.classify.find", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            if (numeric.toInt(parameter.get("key")) > 4)
                data.putAll(parameter);
            json.put("data", data);

            return json.toJSONString();
        });

        JSONArray array = new JSONArray();
        Assert.assertEquals(array.hashCode(), classifyHelper.fill(array, null, null).hashCode());
        Assert.assertTrue(array.isEmpty());

        for (int i = 0; i < 10; i++) {
            JSONObject object = new JSONObject();
            object.put("name", "name " + i);
            object.put("state", "" + i);
            object.put("type", "" + i);
            array.add(object);
        }
        Assert.assertEquals(array.hashCode(), classifyHelper.fill(array, null, null).hashCode());
        noChange(array, 10);
        Assert.assertEquals(array.hashCode(), classifyHelper.fill(array, "code", null).hashCode());
        noChange(array, 10);

        array = classifyHelper.fill(array, "code", new String[]{"state", "type"});
        noChange(array, 5);
        for (int i = 5; i < 10; i++) {
            JSONObject object = array.getJSONObject(i);
            Assert.assertEquals("name " + i, object.getString("name"));
            for (String name : new String[]{"state", "type"}) {
                JSONObject obj = object.getJSONObject(name);
                Assert.assertEquals(2, obj.size());
                Assert.assertEquals("code", obj.getString("code"));
                Assert.assertEquals("" + i, obj.getString("key"));
            }
        }
    }

    private void noChange(JSONArray array, int size) {
        for (int i = 0; i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            Assert.assertEquals("name " + i, object.getString("name"));
            Assert.assertEquals("" + i, object.getString("state"));
            Assert.assertEquals("" + i, object.getString("type"));
        }
    }

    @Test
    public void save() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        Map<String, String> map = new HashMap<>();
        mockCarousel.register("ranch.classify.save", (key, header, parameter, cacheTime) -> {
            map.putAll(parameter);

            return "{\"code\":0,\"data\":{\"id\":\"id value\"}}";
        });
        JSONObject object = classifyHelper.save("code 1", "key 1", "value 1", "name 1");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id value", object.getString("id"));
        Assert.assertEquals(6, map.size());
        Assert.assertEquals("code 1", map.get("code"));
        Assert.assertEquals("key 1", map.get("key"));
        Assert.assertEquals("value 1", map.get("value"));
        Assert.assertEquals("name 1", map.get("name"));
        Assert.assertTrue(map.containsKey("sign"));
    }
}
