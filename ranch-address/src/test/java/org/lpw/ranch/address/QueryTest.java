package org.lpw.ranch.address;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.dao.model.ModelHelper;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Inject
    private ModelHelper modelHelper;

    @Test
    public void query() {
        for (int i = 0; i < 10; i++)
            create("user " + (i % 2), i, i % 3);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/address/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 0\"}}");
        mockHelper.reset();
        mockHelper.mock("/address/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertEquals(5, data.size());
        equals(data.getJSONObject(0), "user 0", 2, 2);
        equals(data.getJSONObject(1), "user 0", 8, 2);
        equals(data.getJSONObject(2), "user 0", 4, 1);
        equals(data.getJSONObject(3), "user 0", 0, 0);
        equals(data.getJSONObject(4), "user 0", 6, 0);

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 1\"}}");
        mockHelper.reset();
        mockHelper.mock("/address/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(5, data.size());
        equals(data.getJSONObject(0), "user 1", 5, 2);
        equals(data.getJSONObject(1), "user 1", 1, 1);
        equals(data.getJSONObject(2), "user 1", 7, 1);
        equals(data.getJSONObject(3), "user 1", 3, 0);
        equals(data.getJSONObject(4), "user 1", 9, 0);

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 2\"}}");
        mockHelper.reset();
        mockHelper.mock("/address/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(0, data.size());
    }
}
