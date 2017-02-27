package org.lpw.ranch.friend;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Test
    public void query() {
        for (int i = 0; i < 10; i++)
            create("owner " + (i % 2), i);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/friend/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"user id\"}}");
        mockHelper.reset();
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONArray("data").isEmpty());

        List<FriendModel> list1 = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list1.add(create("user id", i));
        List<FriendModel> list2 = new ArrayList<>();
        for (int i = 5; i < 10; i++)
            list2.add(create("another user id", i));
        List<FriendModel> list3 = new ArrayList<>();
        list3.add(create("new user id", "friend 10", null, 10));
        for (int i = 11; i < 13; i++)
            list3.add(create("new user id", i));
        for (int i = 13; i < 15; i++)
            list3.add(create("new user id", "friend " + i, null, i));

        mockHelper.reset();
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONArray("data").isEmpty());

        cache.remove(FriendModel.NAME + ".service.owner:user id");
        mockHelper.reset();
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(list1, object.getJSONArray("data"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"another user id\"}}");
        mockHelper.reset();
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(list2, object.getJSONArray("data"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"new user id\"}}");
        mockCarousel.register("ranch.user.get", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            String id = parameter.get("ids");
            JSONObject obj = new JSONObject();
            obj.put("id", id);
            if (!"friend 10".equals(id))
                obj.put("nick", "nick " + id);
            JSONObject data = new JSONObject();
            data.put(id, obj);
            json.put("data", data);

            return json.toJSONString();
        });
        mockHelper.reset();
        mockHelper.mock("/friend/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(list3, object.getJSONArray("data"));
    }
}
