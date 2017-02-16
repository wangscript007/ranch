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

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Calendar;

/**
 * @helper lpw
 */
public class ServiceHelperSupportTest extends TephraTestSupport {
    @Inject
    private Thread thread;
    @Inject
    private MockHelper mockHelper;
    @Inject
    private MockCarousel mockCarousel;
    @Resource(name = "ranch.util.service-helper")
    private ServiceHelperSupport serviceHelper;

    @Test
    public void get() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/service-helper");
        mockCarousel.reset();
        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        JSONObject object = serviceHelper.get("id 1");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id 1", object.getString("id"));

        mockCarousel.register("key.get", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        object = serviceHelper.get("id 1");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("new id", object.getString("id"));
        Assert.assertEquals("carousel", object.getString("name"));
    }

    @Test
    public void fill() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/service-helper");
        mockCarousel.reset();
        mockCarousel.register("key.get", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            JSONObject service = new JSONObject();
            service.put("id", parameter.get("ids"));
            service.put("name", "name " + service.getString("id"));
            data.put(service.getString("id"), service);
            json.put("data", data);

            return json.toJSONString();
        });

        Assert.assertNull(serviceHelper.fill(null, null));

        JSONArray array = new JSONArray();
        Assert.assertTrue(serviceHelper.fill(array, null).isEmpty());
        Assert.assertTrue(array.isEmpty());

        String[] keys = new String[]{"service", "name", "helper"};
        for (int i = 0; i < 10; i++) {
            JSONObject object = new JSONObject();
            for (String key : keys)
                object.put(key, key + " " + i);
            array.add(object);
        }
        Assert.assertEquals(array.hashCode(), serviceHelper.fill(array, null).hashCode());
        for (int i = 0; i < 10; i++) {
            JSONObject object = array.getJSONObject(i);
            for (String key : keys)
                Assert.assertEquals(key + " " + i, object.getString(key));
        }

        Assert.assertEquals(array.hashCode(), serviceHelper.fill(array, new String[0]).hashCode());
        for (int i = 0; i < 10; i++) {
            JSONObject object = array.getJSONObject(i);
            for (String key : keys)
                Assert.assertEquals(key + " " + i, object.getString(key));
        }

        array = serviceHelper.fill(array, new String[]{"service", "helper"});
        for (int i = 0; i < 10; i++) {
            JSONObject object = array.getJSONObject(i);
            JSONObject obj = object.getJSONObject("service");
            Assert.assertEquals("service " + i, obj.getString("id"));
            Assert.assertEquals("name service " + i, obj.getString("name"));
            Assert.assertEquals("name " + i, object.getString("name"));
            obj = object.getJSONObject("helper");
            Assert.assertEquals("helper " + i, obj.getString("id"));
            Assert.assertEquals("name helper " + i, obj.getString("name"));
        }
    }
}
