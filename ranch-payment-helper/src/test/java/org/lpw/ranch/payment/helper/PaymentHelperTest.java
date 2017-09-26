package org.lpw.ranch.payment.helper;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
public class PaymentHelperTest extends TephraTestSupport {
    @Inject
    private MockHelper mockHelper;
    @Inject
    private MockCarousel mockCarousel;
    @Inject
    private PaymentHelper paymentHelper;

    @Test
    public void create() {
        mockHelper.reset();
        mockHelper.getRequest().addParameter("payment", "helper");

        Map<String, String> map = new HashMap<>();
        mockCarousel("create", 1, "orderNo", map);
        Assert.assertNull(paymentHelper.create("type", "app id", "user", 1, "notice", null));
        Assert.assertEquals(6, map.size());
        Assert.assertEquals("helper", map.get("payment"));
        Assert.assertEquals("type", map.get("type"));
        Assert.assertEquals("user", map.get("user"));
        Assert.assertEquals("app id", map.get("appId"));
        Assert.assertEquals("1", map.get("amount"));
        Assert.assertEquals("notice", map.get("notice"));

        map.clear();
        mockCarousel("create", 0, "id", map);
        Assert.assertNull(paymentHelper.create("type", "app id", "user", 1, "notice", new HashMap<>()));
        Assert.assertEquals(6, map.size());
        Assert.assertEquals("helper", map.get("payment"));
        Assert.assertEquals("type", map.get("type"));
        Assert.assertEquals("user", map.get("user"));
        Assert.assertEquals("app id", map.get("appId"));
        Assert.assertEquals("1", map.get("amount"));
        Assert.assertEquals("notice", map.get("notice"));

        map.clear();
        mockCarousel("create", 0, "orderNo", map);
        Map<String, String> m = new HashMap<>();
        m.put("name", "value");
        Assert.assertEquals("order no", paymentHelper.create("type", "app id", "user", 1, "notice", m));
        Assert.assertEquals(7, map.size());
        Assert.assertEquals("helper", map.get("payment"));
        Assert.assertEquals("type", map.get("type"));
        Assert.assertEquals("user", map.get("user"));
        Assert.assertEquals("app id", map.get("appId"));
        Assert.assertEquals("1", map.get("amount"));
        Assert.assertEquals("notice", map.get("notice"));
        Assert.assertEquals("value", map.get("name"));
    }

    @Test
    public void complete() {
        mockHelper.reset();
        mockHelper.getRequest().addParameter("payment", "helper");

        Map<String, String> map = new HashMap<>();
        mockCarousel("complete", 1, "orderNo", map);
        Assert.assertNull(paymentHelper.complete("order no", 1, "trade no", 2, null));
        Assert.assertEquals(5, map.size());
        Assert.assertEquals("helper", map.get("payment"));
        Assert.assertEquals("order no", map.get("orderNo"));
        Assert.assertEquals("1", map.get("amount"));
        Assert.assertEquals("trade no", map.get("tradeNo"));
        Assert.assertEquals("2", map.get("state"));

        map.clear();
        mockCarousel("complete", 0, "id", map);
        Assert.assertNull(paymentHelper.complete("order no", 1, "trade no", 2, new HashMap<>()));
        Assert.assertEquals(5, map.size());
        Assert.assertEquals("helper", map.get("payment"));
        Assert.assertEquals("order no", map.get("orderNo"));
        Assert.assertEquals("1", map.get("amount"));
        Assert.assertEquals("trade no", map.get("tradeNo"));
        Assert.assertEquals("2", map.get("state"));

        map.clear();
        mockCarousel("complete", 0, "orderNo", map);
        Map<String, String> m = new HashMap<>();
        m.put("name", "value");
        Assert.assertEquals("order no", paymentHelper.complete("order no", 1, "trade no", 2, m));
        Assert.assertEquals(6, map.size());
        Assert.assertEquals("helper", map.get("payment"));
        Assert.assertEquals("order no", map.get("orderNo"));
        Assert.assertEquals("1", map.get("amount"));
        Assert.assertEquals("trade no", map.get("tradeNo"));
        Assert.assertEquals("2", map.get("state"));
        Assert.assertEquals("value", map.get("name"));
    }

    private void mockCarousel(String name, int code, String property, Map<String, String> map) {
        mockCarousel.reset();
        mockCarousel.register("ranch.payment." + name, (key, header, parameter, cacheTime) -> {
            map.putAll(parameter);
            JSONObject json = new JSONObject();
            json.put("code", code);
            JSONObject data = new JSONObject();
            data.put(property, "order no");
            json.put("data", data);

            return json.toJSONString();
        });
    }
}
