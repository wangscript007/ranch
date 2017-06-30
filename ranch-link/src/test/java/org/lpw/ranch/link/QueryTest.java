package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.PageTester;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Inject
    private PageTester pageTester;

    @Test
    public void query() {
        for (int i = 0; i < 10; i++)
            create(i, 0, 1, i);
        for (int i = 0; i < 10; i++)
            create(i, 1, i, 1);

        mockHelper.reset();
        mockHelper.mock("/link/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        JSONObject data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 20, 1, data);
        JSONArray list = data.getJSONArray("list");
        Assert.assertEquals(0, list.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 0");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.mock("/link/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 10, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(0, list.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 0");
        mockHelper.getRequest().addParameter("id1", "id1 0");
        mockHelper.mock("/link/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 20, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(0, list.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 0");
        mockHelper.getRequest().addParameter("id1", "id1 1");
        mockHelper.mock("/link/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(10, 20, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(10, list.size());
        for (int i = 0; i < 10; i++)
            equals(list.getJSONObject(i), i, 0, 1, i);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 1");
        mockHelper.getRequest().addParameter("id2", "id2 1");
        mockHelper.mock("/link/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(10, 20, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(10, list.size());
        for (int i = 0; i < 10; i++)
            equals(list.getJSONObject(i), i, 1, i, 1);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 1");
        mockHelper.getRequest().addParameter("id1", "id1 1");
        mockHelper.getRequest().addParameter("id2", "id2 2");
        mockHelper.mock("/link/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(1, 20, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(1, list.size());
        equals(list.getJSONObject(0), 1, 1, 1, 1);
    }
}
