package org.lpw.ranch.user.helper;

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
    private ServiceHelperTester serviceHelperTester;
    @Inject
    private UserHelper userHelper;

    @Test
    public void get() {
        serviceHelperTester.get((UserHelperImpl) userHelper, "ranch.user");
    }

    @Test
    public void findByCode() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        JSONObject object = userHelper.findByCode("code value");
        Assert.assertTrue(object.isEmpty());

        mockCarousel.register("ranch.user.find-by-code", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            data.putAll(parameter);
            data.put("cacheTime", cacheTime);
            json.put("data", data);

            return json.toJSONString();
        });
        object = userHelper.findByCode("code value");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("code value", object.getString("code"));
        Assert.assertEquals(5, object.getIntValue("cacheTime"));
    }

    @Test
    public void findByUid() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        JSONObject object = userHelper.findByUid("code value");
        Assert.assertTrue(object.isEmpty());

        mockCarousel.register("ranch.user.find-by-uid", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            data.putAll(parameter);
            data.put("cacheTime", cacheTime);
            json.put("data", data);

            return json.toJSONString();
        });
        object = userHelper.findByUid("uid value");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("uid value", object.getString("uid"));
        Assert.assertEquals(5, object.getIntValue("cacheTime"));
    }

    @Test
    public void findIdByUid() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        JSONObject object = userHelper.findByUid("code value");
        Assert.assertTrue(object.isEmpty());

        mockCarousel.register("ranch.user.find-by-uid", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            data.putAll(parameter);
            data.put("cacheTime", cacheTime);
            data.put("id", "id value");
            json.put("data", data);

            return json.toJSONString();
        });
        Assert.assertEquals("id value", userHelper.findIdByUid("uid value"));
    }

    @Test
    public void fill() {
        serviceHelperTester.fill((UserHelperImpl) userHelper, "ranch.user");
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

    @Test
    public void id() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockHelper.reset();
        mockHelper.mock("/carousel");
        mockCarousel.reset();
        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        Assert.assertNull(userHelper.id());

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"id value\"}}");
        Assert.assertEquals("id value", userHelper.id());
    }
}
