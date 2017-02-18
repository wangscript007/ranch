package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.util.ServiceHelperTester;
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
public class ClassifyHelperTest extends TephraTestSupport {
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

        JSONArray array = classifyHelper.list("key prefix", 2);
        Assert.assertEquals(1, array.size());
        JSONObject object = array.getJSONObject(0);
        Assert.assertEquals(4, object.size());
        Assert.assertEquals("header value", object.getString("header name"));
        Assert.assertEquals("key prefix", object.getString("key"));
        Assert.assertEquals("2", object.getString("pageSize"));
        Assert.assertEquals("5", object.getString("cacheTime"));
    }

    @Test
    public void fill() {
        serviceHelperTester.fill((ClassifyHelperImpl) classifyHelper, "ranch.classify");
    }
}
