package org.lpw.ranch.address;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lpw
 */
public class UseTest extends TestSupport {
    @Test
    public void use() {
        AddressModel address1 = create(1);
        AddressModel address2 = create(2);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/address/use");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.mock("/address/use");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/address/use");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", address1.getId());
        mockHelper.mock("/address/use");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 2\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", address1.getId());
        mockHelper.mock("/address/use");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 1\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", address1.getId());
        mockHelper.mock("/address/use");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        equals(data, "user 1", 1, 1, false);
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toTime(data.getString("time")).getTime() < 2000L);
        AddressModel address11 = liteOrm.findById(AddressModel.class, address1.getId());
        equals(address11, "user 1", 1, 1, false);
        Assert.assertTrue(System.currentTimeMillis() - address11.getTime().getTime() < 2000L);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 2\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", address2.getId());
        mockHelper.mock("/address/use");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        equals(data, "user 2", 2, 2, false);
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toTime(data.getString("time")).getTime() < 2000L);
        AddressModel address22 = liteOrm.findById(AddressModel.class, address2.getId());
        equals(address22, "user 2", 2, 2, false);
        Assert.assertTrue(System.currentTimeMillis() - address22.getTime().getTime() < 2000L);
    }
}
