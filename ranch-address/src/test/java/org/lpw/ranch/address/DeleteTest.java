package org.lpw.ranch.address;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class DeleteTest extends TestSupport {
    @Test
    public void delete() {
        List<AddressModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(create("user " + (i % 2), i, i));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/address/delete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.mock("/address/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/address/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/address/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 1\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/address/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2101, object.getIntValue("code"));
        Assert.assertEquals(message.get(AddressModel.NAME + ".update.disabled"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user 0\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/address/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertNull(liteOrm.findById(AddressModel.class, list.get(0).getId()));
        for (int i = 1; i < 10; i++)
            equals(liteOrm.findById(AddressModel.class, list.get(i).getId()), "user " + (i % 2), i, i, true);
    }
}
