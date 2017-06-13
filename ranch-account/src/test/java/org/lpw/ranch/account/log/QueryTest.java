package org.lpw.ranch.account.log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.TimeUnit;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Test
    public void query() {
        List<LogModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(create(i, i % 4, i % 5 == 0, 0L));

        mockHelper.reset();
        mockHelper.mock("/account/log/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockUser();
        mockHelper.reset();
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(10, 20, 1, data);
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(10, array.size());
        for (int i = 0; i < 10; i++) {
            JSONObject obj = array.getJSONObject(i);
            Assert.assertEquals(list.get(i).getId(), obj.getString("id"));
            equals(obj, i, i % 4, i % 5 == 0);
        }

        mockCarousel.reset();
        mockCarousel.register("ranch.user.find-by-uid", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid value");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(0, array.size());

        mockUser();
        mockCarousel.register("ranch.user.find-by-uid", "{\"code\":0,\"data\":{\"id\":\"user 1\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("uid", "uid 1");
        mockHelper.getRequest().addParameter("type", "type 1");
        mockHelper.getRequest().addParameter("state", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(1, array.size());
        JSONObject obj = array.getJSONObject(0);
        Assert.assertEquals(list.get(1).getId(), obj.getString("id"));
        equals(obj, 1, 1, false);

        mockUser();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("state", "2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(2, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(2, array.size());
        for (int i = 0, j = 0; i < 10; i++) {
            if (i % 4 != 2)
                continue;

            obj = array.getJSONObject(j++);
            Assert.assertEquals(list.get(i).getId(), obj.getString("id"));
            equals(obj, i, 2, false);
        }

        liteOrm.delete(new LiteQuery(LogModel.class), null);
        list.clear();
        for (int i = 0; i < 5; i++)
            list.add(create(i, i % 4, false, time - i * TimeUnit.Second.getTime()));
        for (int i = 5; i < 10; i++)
            list.add(create(i, i % 4, false, time - TimeUnit.Day.getTime() - i * TimeUnit.Second.getTime()));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("start", dateTime.toString(dateTime.today()));
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(5, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(5, array.size());
        for (int i = 0; i < 5; i++)
            Assert.assertEquals(list.get(i).getId(), array.getJSONObject(i).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("end", dateTime.toString(new Date(time - TimeUnit.Day.getTime())));
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/account/log/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(5, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(5, array.size());
        for (int i = 0; i < 5; i++)
            Assert.assertEquals(list.get(5 + i).getId(), array.getJSONObject(i).getString("id"));
    }
}
