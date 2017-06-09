package org.lpw.ranch.payment;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
public class NoticeTest extends TestSupport {
    @Test
    @SuppressWarnings({"unchecked"})
    public void notice() {
        mockHelper.reset();
        mockHelper.mock("/payment/notice");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2512, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(PaymentModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/payment/notice");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2512, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(PaymentModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.mock("/payment/notice");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/notice");
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

        PaymentModel payment1 = create(1, 0);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", payment1.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/notice");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals(1, urls.size());
        Assert.assertEquals("notify 1", urls.get(0));
        Assert.assertEquals(1, parameters.size());
        Map<String, String> parameter = (Map<String, String>) parameters.get(0);
        Assert.assertEquals("type 1", parameter.get("type"));
        Assert.assertEquals("user 1", parameter.get("user"));
        Assert.assertEquals("101", parameter.get("amount"));
        Assert.assertEquals("order no 1", parameter.get("orderNo"));
        Assert.assertEquals("0", parameter.get("state"));
        Assert.assertEquals(dateTime.toString(payment1.getStart()), parameter.get("start"));
        Assert.assertEquals(dateTime.toString(payment1.getEnd()), parameter.get("end"));
    }
}
