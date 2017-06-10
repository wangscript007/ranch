package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.audit.Audit;

/**
 * @author lpw
 */
public class QueryByAuthorTest extends TestSupport {
    @Test
    public void queryByAuthor() {
        for (int i = 0; i < 10; i++)
            create(i, "author " + (i % 2), Audit.values()[i % Audit.values().length]);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/doc/query-by-author");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/doc/query-by-author");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 10, 1, data);
        JSONArray list = data.getJSONArray("list");
        Assert.assertTrue(list.isEmpty());

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"author 0\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/doc/query-by-author");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(5, 10, 1, data);
        list = data.getJSONArray("list");
        Assert.assertEquals(5, list.size());
        for (int i = 0; i < 10; i++)
            if (i % 2 == 0)
                equals(list.getJSONObject(i / 2), i, "author 0", Audit.values()[i % Audit.values().length]);
    }
}
