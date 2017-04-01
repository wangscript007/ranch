package org.lpw.ranch.group;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.group.member.MemberModel;

/**
 * @author lpw
 */
public class QueryByUserTest extends TestSupport {
    //    @Test
    public void queryByUser() {
        String[] users = new String[]{generator.uuid(), generator.uuid()};
        create(users, 0, 10);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/group/query-by-user");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.mock("/group/query-by-user");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertTrue(data.isEmpty());

        for (int i = 0; i < 2; i++) {
            if (i == 1)
                create(users, 10, 10);

            mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"" + users[0] + "\"}}");
            mockHelper.reset();
            mockHelper.mock("/group/query-by-user");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            data = object.getJSONArray("data");
            Assert.assertEquals(4, data.size());
            sort(data);
            for (int j = 0; j < 4; j++)
                equals(data.getJSONObject(j), 2 * (j + 1));

            mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"" + users[1] + "\"}}");
            mockHelper.reset();
            mockHelper.mock("/group/query-by-user");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            data = object.getJSONArray("data");
            Assert.assertEquals(4, data.size());
            sort(data);
            for (int j = 0; j < 4; j++)
                equals(data.getJSONObject(j), 2 * (j + 1) + 1);
        }

        for (String user : users)
            cache.remove("ranch.group.member.service.user:" + user);

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"" + users[0] + "\"}}");
        mockHelper.reset();
        mockHelper.mock("/group/query-by-user");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(9, data.size());
        sort(data);
        for (int i = 0; i < 9; i++)
            equals(data.getJSONObject(i), 2 * (i + 1));

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"" + users[1] + "\"}}");
        mockHelper.reset();
        mockHelper.mock("/group/query-by-user");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(9, data.size());
        sort(data);
        for (int i = 0; i < 9; i++)
            equals(data.getJSONObject(i), 2 * (i + 1) + 1);
    }

    private void create(String[] users, int start, int size) {
        for (int i = start; i < start + size; i++) {
            GroupModel group = create(i);
            MemberModel member = new MemberModel();
            member.setGroup(group.getId());
            member.setUser(users[i % 2]);
            member.setType(i >> 1);
            liteOrm.save(member);
        }
    }

    private void sort(JSONArray data) {
        data.sort((Object o1, Object o2) -> {
            JSONObject object1 = (JSONObject) o1;
            JSONObject object2 = (JSONObject) o2;

            return object1.getIntValue("audit") - object2.getIntValue("audit");
        });
    }

    private void equals(JSONObject object, int i) {
        Assert.assertEquals("owner " + i, object.getString("owner"));
        Assert.assertEquals("name " + i, object.getString("name"));
        Assert.assertEquals("note " + i, object.getString("note"));
        Assert.assertEquals(100 + i, object.getIntValue("member"));
        Assert.assertEquals(i, object.getIntValue("audit"));
    }
}
