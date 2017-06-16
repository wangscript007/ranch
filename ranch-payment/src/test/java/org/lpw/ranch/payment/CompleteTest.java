package org.lpw.ranch.payment;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.TimeUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
public class CompleteTest extends TestSupport {
    @Test
    public void complete() {
        mockHelper.reset();
        mockHelper.mock("/payment/complete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2507, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(PaymentModel.NAME + ".orderNo")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2503, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-greater-than", message.get(PaymentModel.NAME + ".amount"), 0), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "-1");
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2503, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-greater-than", message.get(PaymentModel.NAME + ".amount"), 0), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2508, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(PaymentModel.NAME + ".tradeNo")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("tradeNo", generator.random(101));
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2509, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(PaymentModel.NAME + ".tradeNo"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2510, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(PaymentModel.NAME + ".state"), 1, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.getRequest().addParameter("state", "3");
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2510, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(PaymentModel.NAME + ".state"), 1, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.getRequest().addParameter("state", "1");
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        PaymentModel payment1 = create(1, 0);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.getRequest().addParameter("state", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2511, object.getIntValue("code"));
        Assert.assertEquals(message.get(PaymentModel.NAME + ".not-exists"), object.getString("message"));

        httpAspect.reset();
        List<String> urls = new ArrayList<>();
        List<Map<String, String>> headers = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        List<String> contents = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            contents.add("content " + i);
        httpAspect.post(urls, headers, parameters, contents);

        mockCarousel.reset();
        Map<String, String> deposit = new HashMap<>();
        mockCarousel.register("ranch.account.deposit", (key, header, parameter, cacheTime) -> {
            deposit.putAll(parameter);
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            data.put("logId", "log id");
            json.put("data", data);

            return json.toJSONString();
        });
        Map<String, String> pass = new HashMap<>();
        mockCarousel.register("ranch.account.log.pass", (key, header, parameter, cacheTime) -> {
            pass.putAll(parameter);
            JSONObject json = new JSONObject();
            json.put("code", 0);

            return json.toJSONString();
        });
        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", payment1.getId());
        mockHelper.getRequest().addParameter("amount", "2");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.getRequest().addParameter("state", "1");
        mockHelper.getRequest().addParameter("label", "label 1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(10, data.size());
        Assert.assertEquals("type 1", data.getString("type"));
        Assert.assertEquals("user 1", data.getString("user"));
        Assert.assertEquals(2, data.getIntValue("amount"));
        Assert.assertEquals("order no 1", data.getString("orderNo"));
        Assert.assertEquals("trade no", data.getString("tradeNo"));
        Assert.assertEquals(1, data.getIntValue("state"));
        Assert.assertEquals("notify 1", data.getString("notify"));
        long time = dateTime.toTime(data.getString("start")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < TimeUnit.Hour.getTime() + 2000L);
        time = dateTime.toTime(data.getString("end")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        PaymentModel payment11 = liteOrm.findById(PaymentModel.class, payment1.getId());
        Assert.assertEquals("type 1", payment11.getType());
        Assert.assertEquals("user 1", payment11.getUser());
        Assert.assertEquals(2, payment11.getAmount());
        Assert.assertEquals("order no 1", payment11.getOrderNo());
        Assert.assertEquals("trade no", payment11.getTradeNo());
        Assert.assertEquals(1, payment11.getState());
        Assert.assertEquals("notify 1", payment11.getNotify());
        time = payment11.getStart().getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < TimeUnit.Hour.getTime() + 2000L);
        Assert.assertTrue(System.currentTimeMillis() - payment11.getEnd().getTime() < 2000L);
        JSONObject json = this.json.toObject(payment11.getJson());
        Assert.assertEquals(2, json.size());
        Assert.assertEquals("label 1", json.getString("label"));
        Assert.assertEquals("{\"label\":\"label 1\"}", json.getJSONObject("complete").toJSONString());
        Assert.assertEquals(1, urls.size());
        Assert.assertEquals("notify 1", urls.get(0));
        Assert.assertEquals("user 1", deposit.get("user"));
        Assert.assertEquals("", deposit.get("owner"));
        Assert.assertEquals("2", deposit.get("amount"));
        Assert.assertEquals("log id", pass.get("ids"));

        deposit.clear();
        pass.clear();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", payment1.getOrderNo());
        mockHelper.getRequest().addParameter("amount", "3");
        mockHelper.getRequest().addParameter("tradeNo", "trade no");
        mockHelper.getRequest().addParameter("state", "2");
        mockHelper.getRequest().addParameter("label", "label 2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(10, data.size());
        Assert.assertEquals("type 1", data.getString("type"));
        Assert.assertEquals("user 1", data.getString("user"));
        Assert.assertEquals(2, data.getIntValue("amount"));
        Assert.assertEquals("order no 1", data.getString("orderNo"));
        Assert.assertEquals("trade no", data.getString("tradeNo"));
        Assert.assertEquals(1, data.getIntValue("state"));
        Assert.assertEquals("notify 1", data.getString("notify"));
        time = dateTime.toTime(data.getString("start")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < TimeUnit.Hour.getTime() + 2000L);
        time = dateTime.toTime(data.getString("end")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        PaymentModel payment111 = liteOrm.findById(PaymentModel.class, payment1.getId());
        Assert.assertEquals("type 1", payment111.getType());
        Assert.assertEquals("user 1", payment111.getUser());
        Assert.assertEquals(2, payment111.getAmount());
        Assert.assertEquals("order no 1", payment111.getOrderNo());
        Assert.assertEquals("trade no", payment11.getTradeNo());
        Assert.assertEquals(1, payment111.getState());
        Assert.assertEquals("notify 1", payment111.getNotify());
        time = payment111.getStart().getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < TimeUnit.Hour.getTime() + 2000L);
        Assert.assertTrue(System.currentTimeMillis() - payment111.getEnd().getTime() < 2000L);
        json = this.json.toObject(payment11.getJson());
        Assert.assertEquals(2, json.size());
        Assert.assertEquals("label 1", json.getString("label"));
        Assert.assertEquals("{\"label\":\"label 1\"}", json.getJSONObject("complete").toJSONString());
        Assert.assertEquals(1, urls.size());
        Assert.assertTrue(deposit.isEmpty());
        Assert.assertTrue(pass.isEmpty());

        PaymentModel payment2 = create(2, 0);
        urls.clear();
        deposit.clear();
        pass.clear();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", payment2.getId());
        mockHelper.getRequest().addParameter("amount", "2");
        mockHelper.getRequest().addParameter("tradeNo", "trade no 2");
        mockHelper.getRequest().addParameter("state", "2");
        mockHelper.getRequest().addParameter("label", "label 2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/complete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(10, data.size());
        Assert.assertEquals("type 2", data.getString("type"));
        Assert.assertEquals("user 2", data.getString("user"));
        Assert.assertEquals(2, data.getIntValue("amount"));
        Assert.assertEquals("order no 2", data.getString("orderNo"));
        Assert.assertEquals("trade no 2", data.getString("tradeNo"));
        Assert.assertEquals(2, data.getIntValue("state"));
        Assert.assertEquals("notify 2", data.getString("notify"));
        time = dateTime.toTime(data.getString("start")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > 2 * TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < 2 * TimeUnit.Hour.getTime() + 2000L);
        time = dateTime.toTime(data.getString("end")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        PaymentModel payment22 = liteOrm.findById(PaymentModel.class, payment2.getId());
        Assert.assertEquals("type 2", payment22.getType());
        Assert.assertEquals("user 2", payment22.getUser());
        Assert.assertEquals(2, payment22.getAmount());
        Assert.assertEquals("order no 2", payment22.getOrderNo());
        Assert.assertEquals("trade no 2", payment22.getTradeNo());
        Assert.assertEquals(2, payment22.getState());
        Assert.assertEquals("notify 2", payment22.getNotify());
        time = payment22.getStart().getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > 2 * TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < 2 * TimeUnit.Hour.getTime() + 2000L);
        Assert.assertTrue(System.currentTimeMillis() - payment11.getEnd().getTime() < 2000L);
        json = this.json.toObject(payment22.getJson());
        Assert.assertEquals(2, json.size());
        Assert.assertEquals("label 2", json.getString("label"));
        Assert.assertEquals("{\"label\":\"label 2\"}", json.getJSONObject("complete").toJSONString());
        Assert.assertEquals(1, urls.size());
        Assert.assertEquals("notify 2", urls.get(0));
        Assert.assertTrue(deposit.isEmpty());
        Assert.assertTrue(pass.isEmpty());
    }
}
