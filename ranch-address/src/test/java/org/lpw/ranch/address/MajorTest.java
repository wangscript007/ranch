package org.lpw.ranch.address;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class MajorTest extends TestSupport {
    @Test
    public void major() {
        List<AddressModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(create("user " + (i % 2), i, i));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/address/major");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.mock("/address/major");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/address/major");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/address/major");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 1\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/address/major");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 0\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/address/major");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        equals(data, "user 0", 0, 1, false);
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toTime(data.getString("time")).getTime() < 2000L);
        for (int i = 1; i < 10; i++)
            equals(liteOrm.findById(AddressModel.class, list.get(i).getId()), "user " + (i % 2), i, i % 2 == 0 ? 0 : i, true);
    }
}
