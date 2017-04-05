package org.lpw.ranch.last.helper;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
public class LastHelperTest extends TephraTestSupport {
    @Inject
    private MockCarousel mockCarousel;
    @Inject
    private LastHelper lastHelper;

    @Test
    public void find() {
        mockCarousel.reset();
        mockCarousel.register("ranch.last.find", (String key, Map<String, String> header, Map<String, String> parameter, int cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            data.putAll(parameter);
            json.put("data", data);

            return json.toJSONString();
        });

        JSONObject object = lastHelper.find("type value");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("type value", object.getString("type"));
    }

    @Test
    public void save() {
        mockCarousel.reset();
        mockCarousel.register("ranch.last.save", (String key, Map<String, String> header, Map<String, String> parameter, int cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            data.putAll(parameter);
            json.put("data", data);

            return json.toJSONString();
        });

        JSONObject object = lastHelper.save("type value", null);
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("type value", object.getString("type"));

        Map<String, String> map = new HashMap<>();
        map.put("name", "name value");
        object = lastHelper.save("type value", map);
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("type value", object.getString("type"));
        Assert.assertEquals("name value", object.getString("name"));
    }
}
