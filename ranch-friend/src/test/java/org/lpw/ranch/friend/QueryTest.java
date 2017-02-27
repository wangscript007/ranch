package org.lpw.ranch.friend;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Test
    public void query() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("state", "-1");
        mockHelper.mock("/friend/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1605, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(FriendModel.NAME + ".state"), 0, 3), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("state", "4");
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1605, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(FriendModel.NAME + ".state"), 0, 3), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"owner 0\"}}");
        mockHelper.reset();
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONArray("data").isEmpty());

        List<FriendModel> list1 = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list1.add(create("owner 0", 0, i));
        List<FriendModel> list2 = new ArrayList<>();
        for (int i = 5; i < 10; i++)
            list2.add(create("owner 0", 1, i));
        List<FriendModel> list3 = new ArrayList<>();
        list3.add(create("owner 0", "friend 10", null, 2, 0));
        for (int i = 11; i < 13; i++)
            list3.add(create("owner 0", 2, i));
        for (int i = 13; i < 15; i++)
            list3.add(create("owner 0", "friend " + i, null, 2, 0));
        List<FriendModel> list4 = new ArrayList<>();
        for (int i = 15; i < 20; i++)
            list4.add(create("owner 0", 3, i));
        List<FriendModel> list5 = new ArrayList<>();
        for (int i = 20; i < 25; i++)
            list5.add(create("owner 1", 1, i));

        mockHelper.reset();
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONArray("data").isEmpty());

        cache.remove(FriendModel.NAME + ".service.owner:owner 00");
        mockHelper.reset();
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(list1, object.getJSONArray("data"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("state", "1");
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(list2, object.getJSONArray("data"));

        mockCarousel.register("ranch.user.get", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject obj = new JSONObject();
            String id = parameter.get("ids");
            obj.put("id", id);
            if (!"friend 10".equals(id))
                obj.put("nick", "nick " + id);
            JSONObject data = new JSONObject();
            data.put(id, obj);
            json.put("data", data);

            return json.toJSONString();
        });
        mockHelper.reset();
        mockHelper.getRequest().addParameter("state", "2");
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(list3, object.getJSONArray("data"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("state", "3");
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(list4, object.getJSONArray("data"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"owner 1\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("state", "1");
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(list5, object.getJSONArray("data"));
    }
}
