package org.lpw.ranch.group;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.group.member.MemberModel;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class QueryByUserTest extends TestSupport {
    @Test
    public void queryByUser() {
        List<GroupModel> list = new ArrayList<>();
        String[] users = new String[]{generator.uuid(), generator.uuid()};
        for (int i = 0; i < 10; i++) {
            GroupModel group = create(i);
            MemberModel member = new MemberModel();
            member.setGroup(group.getId());
            member.setUser(users[i % 2]);
            member.setType(i >> 1);
            liteOrm.save(member);
            list.add(group);
        }

        mockHelper.reset();
        mockHelper.mock("/group/query-by-user");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1706, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(GroupModel.NAME + ".user")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user id");
        mockHelper.mock("/group/query-by-user");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1706, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(GroupModel.NAME + ".user")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", generator.uuid());
        mockHelper.mock("/group/query-by-user");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertTrue(data.isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", users[0]);
        mockHelper.mock("/group/query-by-user");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(4, data.size());
        sort(data);
        for (int i = 0; i < 4; i++) {
            int j = 2 * (i + 1);
            JSONObject obj = data.getJSONObject(i);
            Assert.assertEquals("owner " + j, obj.getString("owner"));
            Assert.assertEquals("name " + j, obj.getString("name"));
            Assert.assertEquals("note " + j, obj.getString("note"));
            Assert.assertEquals(100 + j, obj.getIntValue("member"));
            Assert.assertEquals(j, obj.getIntValue("audit"));
        }

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", users[1]);
        mockHelper.mock("/group/query-by-user");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(4, data.size());
        sort(data);
        for (int i = 0; i < 4; i++) {
            int j = 2 * (i + 1) + 1;
            JSONObject obj = data.getJSONObject(i);
            Assert.assertEquals("owner " + j, obj.getString("owner"));
            Assert.assertEquals("name " + j, obj.getString("name"));
            Assert.assertEquals("note " + j, obj.getString("note"));
            Assert.assertEquals(100 + j, obj.getIntValue("member"));
            Assert.assertEquals(j, obj.getIntValue("audit"));
        }
    }

    private void sort(JSONArray data) {
        data.sort((Object o1, Object o2) -> {
            JSONObject object1 = (JSONObject) o1;
            JSONObject object2 = (JSONObject) o2;

            return object1.getIntValue("audit") - object2.getIntValue("audit");
        });
    }
}
