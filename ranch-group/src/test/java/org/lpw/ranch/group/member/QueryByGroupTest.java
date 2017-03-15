package org.lpw.ranch.group.member;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.group.GroupModel;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.Comparator;

/**
 * @author lpw
 */
public class QueryByGroupTest extends TestSupport {
    @Test
    public void queryByGroup() {
        String[] groups = new String[2];
        for (int i = 0; i < groups.length; i++) {
            GroupModel group = new GroupModel();
            group.setOwner("owner " + i);
            group.setCreate(dateTime.now());
            liteOrm.save(group);
            groups[i] = group.getId();

            for (int j = 0; j < 5; j++)
                create(group.getId(), i, j);
        }

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.mock("/group/member/query-by-group");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1728, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(MemberModel.NAME + ".group")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", "group id");
        mockHelper.mock("/group/member/query-by-group");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1728, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(MemberModel.NAME + ".group")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", generator.uuid());
        mockHelper.mock("/group/member/query-by-group");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONArray("data").isEmpty());

        mockCarousel.register("ranch.user.get", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            String id = parameter.get("ids");
            JSONObject user = new JSONObject();
            user.put("id", id);
            user.put("name", "name " + id);
            data.put(id, user);
            json.put("data", data);

            return json.toJSONString();
        });
        for (int i = 0; i < 2; i++) {
            if (i == 1)
                for (int j = 0; j < 2; j++)
                    create(groups[j], j, 5);

            for (int j = 0; j < 2; j++)
                success(groups, j, 4);
        }

        for (String group : groups)
            cache.remove(MemberModel.NAME + ".service.group:" + group);
        for (int i = 0; i < 2; i++)
            success(groups, i, 5);
    }

    private void create(String group, int i, int j) {
        MemberModel member = new MemberModel();
        member.setGroup(group);
        member.setUser("user " + i + j);
        member.setNick("nick " + i + j);
        member.setType(j);
        member.setJoin(dateTime.now());
        liteOrm.save(member);
    }

    private void success(String[] groups, int i, int size) {
        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", groups[i]);
        mockHelper.mock("/group/member/query-by-group");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertEquals(size, data.size());
        data.sort(Comparator.comparingInt((obj) -> ((JSONObject) obj).getIntValue("type")));
        for (int j = 0; j < size; j++)
            equals(data.getJSONObject(j), i, j + 1);
    }

    private void equals(JSONObject object, int i, int j) {
        Assert.assertTrue(object.containsKey("id"));
        JSONObject user = object.getJSONObject("user");
        Assert.assertEquals("user " + i + j, user.getString("id"));
        Assert.assertEquals("name user " + i + j, user.getString("name"));
        Assert.assertEquals("nick " + i + j, object.getString("nick"));
        Assert.assertEquals(j, object.getIntValue("type"));
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toDate(object.getString("join")).getTime() < 2000L);
    }
}
