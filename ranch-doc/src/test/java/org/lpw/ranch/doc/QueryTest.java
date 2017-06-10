package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Test
    public void query() {
        for (int i = 0; i < 10; i++)
            create(i, Audit.values()[i % Audit.values().length]);

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "-1");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/doc/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1413, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.audit.illegal"), object.getString("message"));

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "3");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/doc/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1413, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.audit.illegal"), object.getString("message"));

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/doc/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(4, 10, 1, data);
        JSONArray list = data.getJSONArray("list");
        Assert.assertEquals(4, list.size());
        for (int i = 0; i < 10; i++)
            if (i % 3 == 0)
                equals(list.getJSONObject(i / 3), i, Audit.Normal);

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(3, 10, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(3, list.size());
        for (int i = 0; i < 10; i++)
            if (i % 3 == 1)
                equals(list.getJSONObject(i / 3), i, Audit.Pass);

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.getRequest().addParameter("key", "key 4");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 10, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(1, list.size());
        equals(list.getJSONObject(0), 4, Audit.Pass);

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("owner", "owner 5");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 10, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(1, list.size());
        equals(list.getJSONObject(0), 5, Audit.Reject);

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.getRequest().addParameter("author", "author 9");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 10, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(1, list.size());
        equals(list.getJSONObject(0), 9, Audit.Normal);

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.getRequest().addParameter("subject", "bject");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(4, 10, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(4, list.size());
        for (int i = 0; i < 10; i++)
            if (i % 3 == 0)
                equals(list.getJSONObject(i / 3), i, Audit.Normal);

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.getRequest().addParameter("key", "key 3");
        mockHelper.getRequest().addParameter("subject", "bject");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 10, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(1, list.size());
        equals(list.getJSONObject(0), 3, Audit.Normal);

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "0");
        mockHelper.getRequest().addParameter("key", "key 3");
        mockHelper.getRequest().addParameter("owner", "owner 6");
        mockHelper.getRequest().addParameter("subject", "bject");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 10, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(0, list.size());
    }
}
