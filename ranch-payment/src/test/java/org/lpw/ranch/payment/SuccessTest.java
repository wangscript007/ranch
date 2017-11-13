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
public class SuccessTest extends TestSupport {
    @Test
    public void success() {
        mockHelper.reset();
        mockHelper.mock("/payment/success");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2501, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(PaymentModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/payment/success");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2501, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(PaymentModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.mock("/payment/success");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/success");
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

        JSONObject notice = new JSONObject();
        notice.put("http", "notice 1");
        PaymentModel payment1 = create(1, 0, notice.toJSONString());
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", payment1.getId());
        mockHelper.getRequest().addParameter("label", "label 1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/success");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(11, data.size());
        Assert.assertEquals("type 1", data.getString("type"));
        Assert.assertEquals("user 1", data.getString("user"));
        Assert.assertEquals(101, data.getIntValue("amount"));
        Assert.assertEquals("order no 1", data.getString("orderNo"));
        Assert.assertEquals("bill no 1", data.getString("billNo"));
        Assert.assertEquals("trade no 1", data.getString("tradeNo"));
        Assert.assertEquals(1, data.getIntValue("state"));
        Assert.assertEquals(notice.toJSONString(), data.getString("notice"));
        long time = dateTime.toTime(data.getString("start")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < TimeUnit.Hour.getTime() + 2000L);
        time = dateTime.toTime(data.getString("end")).getTime();
        Assert.assertTrue(System.currentTimeMillis() - time < 2000L);
        PaymentModel payment11 = liteOrm.findById(PaymentModel.class, payment1.getId());
        Assert.assertEquals("type 1", payment11.getType());
        Assert.assertEquals("user 1", payment11.getUser());
        Assert.assertEquals(101, payment11.getAmount());
        Assert.assertEquals("order no 1", payment11.getOrderNo());
        Assert.assertEquals("bill no 1", payment11.getBillNo());
        Assert.assertEquals("trade no 1", payment11.getTradeNo());
        Assert.assertEquals(1, payment11.getState());
        Assert.assertEquals(notice.toJSONString(), payment11.getNotice());
        time = payment11.getStart().getTime();
        Assert.assertTrue(System.currentTimeMillis() - time > TimeUnit.Hour.getTime() - 2000L);
        Assert.assertTrue(System.currentTimeMillis() - time < TimeUnit.Hour.getTime() + 2000L);
        Assert.assertTrue(System.currentTimeMillis() - payment11.getEnd().getTime() < 2000L);
        JSONObject json = this.json.toObject(payment11.getJson());
        Assert.assertEquals(2, json.size());
        Assert.assertEquals("label 1", json.getString("label"));
        Assert.assertEquals("{\"label\":\"label 1\"}", json.getJSONObject("success").toJSONString());
        Assert.assertEquals(1, urls.size());
        Assert.assertEquals("notice 1", urls.get(0));

        thread.sleep(3, TimeUnit.Second);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", payment1.getId());
        mockHelper.getRequest().addParameter("label", "label 2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/success");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2512, object.getIntValue("code"));
        Assert.assertEquals(message.get(PaymentModel.NAME + ".success.disable"), object.getString("message"));
    }
}
