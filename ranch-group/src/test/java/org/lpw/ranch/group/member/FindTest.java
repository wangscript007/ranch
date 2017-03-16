package org.lpw.ranch.group.member;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lpw
 */
public class FindTest extends TestSupport {
    @Test
    public void find() {
        MemberModel member1 = new MemberModel();
        member1.setGroup("group 1");
        member1.setUser("user 1");
        member1.setType(MemberService.Type.New.ordinal());
        liteOrm.save(member1);
        MemberModel member2 = new MemberModel();
        member2.setGroup("group 2");
        member2.setUser("user 2");
        member2.setType(MemberService.Type.Normal.ordinal());
        liteOrm.save(member2);

        mockHelper.reset();
        mockHelper.mock("/group/member/find");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1729, object.getIntValue("code"));
        Assert.assertEquals(message.get(MemberModel.NAME + ".user.not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", "group 1");
        mockHelper.getRequest().addParameter("user", "user 1");
        mockHelper.mock("/group/member/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1729, object.getIntValue("code"));
        Assert.assertEquals(message.get(MemberModel.NAME + ".user.not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", "group 2");
        mockHelper.getRequest().addParameter("user", "user 2");
        mockHelper.mock("/group/member/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals(member2.getId(), object.getJSONObject("data").getString("id"));
    }
}
