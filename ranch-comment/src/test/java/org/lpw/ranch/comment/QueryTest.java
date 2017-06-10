package org.lpw.ranch.comment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.ctrl.validate.Validators;
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
        List<CommentModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(create(i, Audit.values()[i % 3]));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "-1");
        mockHelper.mock("/comment/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1309, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(CommentModel.NAME + ".audit"), 0, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "3");
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1309, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(CommentModel.NAME + ".audit"), 0, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockCarousel.reset();
        mockUser.register();
        for (int i = 0; i < list.size(); i++) {
            mockCarousel.register("key " + i + ".get", "{\n" +
                    "  \"code\":0,\n" +
                    "  \"data\":{\n" +
                    "    \"owner " + i + "\":{\n" +
                    "      \"id\":\"owner " + i + "\",\n" +
                    "      \"key\":\"owner key " + i + "\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
        }
        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(4, 20, 1, data);
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(4, array.size());
        for (int i = 0; i < array.size(); i++)
            equals(list.get(3 * i), array.getJSONObject(i), 3 * i);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(3, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(3, array.size());
        for (int i = 0; i < array.size(); i++)
            equals(list.get(3 * i + 1), array.getJSONObject(i), 3 * i + 1);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(3, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(3, array.size());
        for (int i = 0; i < array.size(); i++)
            equals(list.get(3 * i + 2), array.getJSONObject(i), 3 * i + 2);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("start", dateTime.toString(new Date(time - 7 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(2, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(2, array.size());
        for (int i = 0; i < array.size(); i++)
            equals(list.get(3 * i + 2), array.getJSONObject(i), 3 * i + 2);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("end", dateTime.toString(new Date(time - 3 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(2, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(2, array.size());
        for (int i = 0; i < array.size(); i++)
            equals(list.get(3 * i + 5), array.getJSONObject(i), 3 * i + 5);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("start", dateTime.toString(new Date(time - 7 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("end", dateTime.toString(new Date(time - 3 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(1, array.size());
        equals(list.get(5), array.getJSONObject(0), 5);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("owner", "owner 5");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(1, array.size());
        equals(list.get(5), array.getJSONObject(0), 5);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("author", "author 5");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(1, array.size());
        equals(list.get(5), array.getJSONObject(0), 5);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("owner", "owner 5");
        mockHelper.getRequest().addParameter("author", "author 5");
        mockHelper.getRequest().addParameter("start", dateTime.toString(new Date(time - 20 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("end", dateTime.toString(new Date(time - 3 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(1, array.size());
        equals(list.get(5), array.getJSONObject(0), 5);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("owner", "owner 5");
        mockHelper.getRequest().addParameter("author", "author 5");
        mockHelper.getRequest().addParameter("start", dateTime.toString(new Date(time - 2 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("end", dateTime.toString(new Date(time - 3 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(0, array.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("owner", "owner 5");
        mockHelper.getRequest().addParameter("author", "author 4");
        mockHelper.getRequest().addParameter("start", dateTime.toString(new Date(time - 20 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("end", dateTime.toString(new Date(time - 3 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(0, array.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("owner", "owner 6");
        mockHelper.getRequest().addParameter("author", "author 5");
        mockHelper.getRequest().addParameter("start", dateTime.toString(new Date(time - 20 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("end", dateTime.toString(new Date(time - 3 * TimeUnit.Day.getTime())));
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(0, array.size());
    }
}
