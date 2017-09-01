package org.lpw.ranch.payment;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class CreateTest extends TestSupport {
    @Test
    public void create() {
        mockHelper.reset();
        mockHelper.mock("/payment/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2502, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(PaymentModel.NAME + ".type")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", generator.random(101));
        mockHelper.mock("/payment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2503, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(PaymentModel.NAME + ".type"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.mock("/payment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2504, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-greater-than", message.get(PaymentModel.NAME + ".amount"), 0), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.getRequest().addParameter("amount", "-1");
        mockHelper.mock("/payment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2504, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-greater-than", message.get(PaymentModel.NAME + ".amount"), 0), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("notice", "notice");
        mockHelper.mock("/payment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2505, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.not-exists-and-not-sign-in", message.get(PaymentModel.NAME + ".user")), object.getString("message"));

        String time = "20170102030405067";
        Timestamp timestamp = dateTime.toTime(time, "yyyyMMddHHmmssSSS");
        List<Timestamp> nows = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            nows.add(timestamp);
        dateTimeAspect.now(nows);
        List<String> randoms = new ArrayList<>();
        randoms.add("session id " + 0);
        randoms.add("0000");
        randoms.add("session id " + 1);
        randoms.add("0000");
        randoms.add("0002");
        generatorAspect.randomString(randoms);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"user id\":{\"id\":\"user id 2\",\"name\":\"user name\"}}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.getRequest().addParameter("user", "user id");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("notice", "notice");
        mockHelper.mock("/payment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(9, data.size());
        Assert.assertEquals("type value", data.getString("type"));
        Assert.assertEquals("user id", data.getString("user"));
        Assert.assertEquals(1, data.getIntValue("amount"));
        Assert.assertEquals(time + "0000", data.getString("orderNo"));
        Assert.assertEquals("", data.getString("tradeNo"));
        Assert.assertEquals(0, data.getIntValue("state"));
        Assert.assertEquals("notice", data.getString("notice"));
        Assert.assertEquals("2017-01-02 03:04:05", data.getString("start"));
        PaymentModel payment1 = findByOrderNo(data.getString("orderNo"));
        Assert.assertEquals("type value", payment1.getType());
        Assert.assertEquals("user id", payment1.getUser());
        Assert.assertEquals(1, payment1.getAmount());
        Assert.assertEquals(time + "0000", payment1.getOrderNo());
        Assert.assertEquals("", payment1.getTradeNo());
        Assert.assertEquals(0, payment1.getState());
        Assert.assertEquals("notice", payment1.getNotice());
        Assert.assertEquals(timestamp.getTime() / 1000, payment1.getStart().getTime() / 1000);
        Assert.assertNull(payment1.getEnd());
        Assert.assertEquals("{\"create\":{}}", payment1.getJson());
        Assert.assertEquals(9, nows.size());
        Assert.assertEquals(3, randoms.size());

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user id 2\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value 2");
        mockHelper.getRequest().addParameter("amount", "2");
        mockHelper.getRequest().addParameter("notice", "notice 2");
        mockHelper.getRequest().addParameter("state", "1");
        mockHelper.getRequest().addParameter("label", "label 2");
        mockHelper.mock("/payment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(9, data.size());
        Assert.assertEquals("type value 2", data.getString("type"));
        Assert.assertEquals("user id 2", data.getString("user"));
        Assert.assertEquals(2, data.getIntValue("amount"));
        Assert.assertEquals(time + "0002", data.getString("orderNo"));
        Assert.assertTrue(data.getString("orderNo").endsWith("0002"));
        Assert.assertEquals("", data.getString("tradeNo"));
        Assert.assertEquals(0, data.getIntValue("state"));
        Assert.assertEquals("notice 2", data.getString("notice"));
        Assert.assertEquals("2017-01-02 03:04:05", data.getString("start"));
        PaymentModel payment2 = findByOrderNo(data.getString("orderNo"));
        Assert.assertEquals("type value 2", payment2.getType());
        Assert.assertEquals("user id 2", payment2.getUser());
        Assert.assertEquals(2, payment2.getAmount());
        Assert.assertEquals(time + "0002", payment2.getOrderNo());
        Assert.assertEquals("", payment2.getTradeNo());
        Assert.assertEquals(0, payment2.getState());
        Assert.assertEquals("notice 2", payment2.getNotice());
        Assert.assertEquals(timestamp.getTime() / 1000, payment2.getStart().getTime() / 1000);
        Assert.assertNull(payment2.getEnd());
        JSONObject json = this.json.toObject(payment2.getJson());
        Assert.assertEquals(1, json.size());
        Assert.assertEquals("{\"label\":\"label 2\"}", json.getJSONObject("create").toJSONString());
        Assert.assertEquals(8, nows.size());
        Assert.assertEquals(0, randoms.size());
        nows.clear();
    }
}
