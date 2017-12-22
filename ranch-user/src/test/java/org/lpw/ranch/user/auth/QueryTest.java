package org.lpw.ranch.user.auth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Message;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author lpw
 */
public class QueryTest extends TephraTestSupport {
    @Inject
    private Message message;
    @Inject
    private Sign sign;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private MockHelper mockHelper;

    @SuppressWarnings({"unchecked"})
    @Test
    public void query() {
        List<AuthModel>[] list = new List[]{new ArrayList<>(), new ArrayList<>()};
        for (int i = 0; i < 10; i++) {
            AuthModel auth = new AuthModel();
            auth.setUser("user " + (i % 2));
            auth.setUid("uid " + i);
            auth.setType(i);
            liteOrm.save(auth);
            list[i % 2].add(auth);
        }
        list[0].sort(Comparator.comparing(AuthModel::getId));
        list[1].sort(Comparator.comparing(AuthModel::getId));

        mockHelper.reset();
        mockHelper.mock("/user/auth/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1551, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(AuthModel.NAME + ".user")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("user", "user 0");
        mockHelper.mock("/user/auth/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        for (int i = 0; i < 2; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("user", "user " + i);
            sign.put(mockHelper.getRequest().getMap(), null);
            mockHelper.mock("/user/auth/query");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            JSONArray array = object.getJSONArray("data");
            Assert.assertEquals(5, array.size());
            for (int j = 0; j < 5; j++) {
                AuthModel model = list[i].get(j);
                JSONObject obj = array.getJSONObject(j);
                Assert.assertEquals("user " + i, obj.getString("user"));
                Assert.assertEquals(model.getUid(), obj.getString("uid"));
                Assert.assertEquals(model.getType(), obj.getIntValue("type"));
            }
        }
    }
}
