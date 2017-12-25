package org.lpw.ranch.transfer;

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
        mockHelper.mock("/transfer/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3003, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(TransferModel.NAME + ".type")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", generator.random(101));
        mockHelper.mock("/transfer/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3004, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(TransferModel.NAME + ".type"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.getRequest().addParameter("appId", generator.random(101));
        mockHelper.mock("/transfer/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3005, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(TransferModel.NAME + ".appId"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.mock("/transfer/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3006, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(TransferModel.NAME + ".account")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.getRequest().addParameter("account", generator.random(101));
        mockHelper.mock("/transfer/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3007, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(TransferModel.NAME + ".account"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.getRequest().addParameter("account", "account value");
        mockHelper.mock("/transfer/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3008, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-greater-than", message.get(TransferModel.NAME + ".amount"), 0), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.getRequest().addParameter("account", "account value");
        mockHelper.getRequest().addParameter("amount", "-1");
        mockHelper.mock("/transfer/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3008, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-greater-than", message.get(TransferModel.NAME + ".amount"), 0), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.getRequest().addParameter("account", "account value");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("billNo", generator.random(101));
        mockHelper.mock("/transfer/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3009, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(TransferModel.NAME + ".billNo"), 100), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.getRequest().addParameter("account", "account value");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("notice", "notice");
        mockHelper.mock("/transfer/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(3010, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.not-exists-and-not-sign-in", message.get(TransferModel.NAME + ".user")), object.getString("message"));

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
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("user", "user id");
        mockHelper.getRequest().addParameter("account", "account value");
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("notice", "notice");
        mockHelper.mock("/transfer/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(12, data.size());
        Assert.assertEquals("type value", data.getString("type"));
        Assert.assertEquals("app id", data.getString("appId"));
        Assert.assertEquals("user id", data.getString("user"));
        Assert.assertEquals("account value", data.getString("account"));
        Assert.assertEquals(1, data.getIntValue("amount"));
        Assert.assertEquals(time + "0000", data.getString("orderNo"));
        Assert.assertEquals("", data.getString("billNo"));
        Assert.assertEquals("", data.getString("tradeNo"));
        Assert.assertEquals(0, data.getIntValue("state"));
        Assert.assertEquals("notice", data.getString("notice"));
        Assert.assertEquals("2017-01-02 03:04:05", data.getString("start"));
        TransferModel transfer1 = findByOrderNo(data.getString("orderNo"));
        Assert.assertEquals("type value", transfer1.getType());
        Assert.assertEquals("user id", transfer1.getUser());
        Assert.assertEquals(1, transfer1.getAmount());
        Assert.assertEquals(time + "0000", transfer1.getOrderNo());
        Assert.assertEquals("", transfer1.getTradeNo());
        Assert.assertEquals(0, transfer1.getState());
        Assert.assertEquals("notice", transfer1.getNotice());
        Assert.assertEquals(timestamp.getTime() / 1000, transfer1.getStart().getTime() / 1000);
        Assert.assertNull(transfer1.getEnd());
        Assert.assertEquals("{\"create\":{}}", transfer1.getJson());
        Assert.assertEquals(9, nows.size());
        Assert.assertEquals(3, randoms.size());

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user id 2\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value 2");
        mockHelper.getRequest().addParameter("account", "account value 2");
        mockHelper.getRequest().addParameter("amount", "2");
        mockHelper.getRequest().addParameter("billNo", "bill no");
        mockHelper.getRequest().addParameter("notice", "notice 2");
        mockHelper.getRequest().addParameter("state", "1");
        mockHelper.getRequest().addParameter("label", "label 2");
        mockHelper.mock("/transfer/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(11, data.size());
        Assert.assertEquals("type value 2", data.getString("type"));
        Assert.assertEquals("user id 2", data.getString("user"));
        Assert.assertEquals("account value 2", data.getString("account"));
        Assert.assertEquals(2, data.getIntValue("amount"));
        Assert.assertEquals(time + "0002", data.getString("orderNo"));
        Assert.assertTrue(data.getString("orderNo").endsWith("0002"));
        Assert.assertEquals("bill no", data.getString("billNo"));
        Assert.assertEquals("", data.getString("tradeNo"));
        Assert.assertEquals(0, data.getIntValue("state"));
        Assert.assertEquals("notice 2", data.getString("notice"));
        Assert.assertEquals("2017-01-02 03:04:05", data.getString("start"));
        TransferModel transfer2 = findByOrderNo(data.getString("orderNo"));
        Assert.assertEquals("type value 2", transfer2.getType());
        Assert.assertEquals("user id 2", transfer2.getUser());
        Assert.assertEquals(2, transfer2.getAmount());
        Assert.assertEquals(time + "0002", transfer2.getOrderNo());
        Assert.assertEquals("", transfer2.getTradeNo());
        Assert.assertEquals(0, transfer2.getState());
        Assert.assertEquals("notice 2", transfer2.getNotice());
        Assert.assertEquals(timestamp.getTime() / 1000, transfer2.getStart().getTime() / 1000);
        Assert.assertNull(transfer2.getEnd());
        JSONObject json = this.json.toObject(transfer2.getJson());
        Assert.assertEquals(1, json.size());
        Assert.assertEquals("{\"label\":\"label 2\"}", json.getJSONObject("create").toJSONString());
        Assert.assertEquals(8, nows.size());
        Assert.assertEquals(0, randoms.size());
        nows.clear();
    }
}
