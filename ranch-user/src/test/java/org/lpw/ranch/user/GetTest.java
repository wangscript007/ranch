package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class GetTest extends TestSupport {
    @Test
    public void get() {
        List<UserModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list.add(create(i));

        mockHelper.reset();
        mockHelper.mock("/user/get");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1521, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(UserModel.NAME + ".ids")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.mock("/user/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/user/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2," + list.get(0).getId() + "," + list.get(1).getId() + "," + list.get(1).getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/user/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(2, data.size());
        for (int i = 0; i < data.size(); i++) {
            UserModel user = list.get(i);
            equals(user, data.getJSONObject(user.getId()));
        }
    }
}
