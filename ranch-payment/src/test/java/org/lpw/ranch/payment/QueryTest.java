package org.lpw.ranch.payment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.TimeUnit;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Test
    public void query() {
        List<PaymentModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(create(i, i % 3));

        mockHelper.reset();
        mockHelper.mock("/payment/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.get", (String key, Map<String, String> header, Map<String, String> parameter, int cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            JSONObject user = new JSONObject();
            user.put("id", parameter.get("ids"));
            user.put("name", "name " + parameter.get("ids"));
            data.put(parameter.get("ids"), user);
            json.put("data", data);

            return json.toJSONString();
        });

        mockHelper.reset();
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(10, 20, 1, data);
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(10, array.size());
        for (int i = 0; i < 10; i++)
            Assert.assertEquals(list.get(i).getId(), array.getJSONObject(i).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("state", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(3, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(3, array.size());
        for (int i = 0, j = 0; i < 10; i++)
            if (i % 3 == 1)
                Assert.assertEquals(list.get(i).getId(), array.getJSONObject(j++).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(1, array.size());
        Assert.assertEquals(list.get(1).getId(), array.getJSONObject(0).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 1");
        mockHelper.getRequest().addParameter("user", "user 1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(1, array.size());
        Assert.assertEquals(list.get(1).getId(), array.getJSONObject(0).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 1");
        mockHelper.getRequest().addParameter("user", "user 2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(0, array.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("orderNo", "order no 2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(1, array.size());
        Assert.assertEquals(list.get(2).getId(), array.getJSONObject(0).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("tradeNo", "trade no 3");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(1, array.size());
        Assert.assertEquals(list.get(3).getId(), array.getJSONObject(0).getString("id"));

        liteOrm.delete(new LiteQuery(PaymentModel.class), null);
        list.clear();
        for (int i = 0; i < 5; i++)
            list.add(create(i, 0, new Timestamp(System.currentTimeMillis() - i * TimeUnit.Minute.getTime())));
        for (int i = 5; i < 10; i++)
            list.add(create(i, 0, new Timestamp(System.currentTimeMillis() - TimeUnit.Day.getTime() - i * TimeUnit.Minute.getTime())));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("start", dateTime.toString(dateTime.today()));
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(5, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(5, array.size());
        for (int i = 0; i < 5; i++)
            Assert.assertEquals(list.get(i).getId(), array.getJSONObject(i).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("start", dateTime.toString(dateTime.today()));
        mockHelper.getRequest().addParameter("end", dateTime.toString(dateTime.today()));
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(5, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(5, array.size());
        for (int i = 0; i < 5; i++)
            Assert.assertEquals(list.get(i).getId(), array.getJSONObject(i).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("end", dateTime.toString(new Date(System.currentTimeMillis() - TimeUnit.Day.getTime())));
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/payment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(5, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(5, array.size());
        for (int i = 0; i < 5; i++)
            Assert.assertEquals(list.get(i + 5).getId(), array.getJSONObject(i).getString("id"));
    }
}
