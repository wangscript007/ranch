package org.lpw.ranch.user;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
        List<UserModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            list.add(create(i));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("mobile", "mobile");
        mockHelper.mock("/user/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1509, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-mobile", message.get(UserModel.NAME + ".mobile")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/user/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/user/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(20, data.getInt("count"));
        Assert.assertEquals(10, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(10, array.size());
        for (int i = 0; i < 10; i++)
            equals(list.get(i), array.getJSONObject(i));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("mobile", "12312345610");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/user/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(0, data.getInt("count"));
        Assert.assertEquals(0, data.getInt("size"));
        Assert.assertEquals(0, data.getInt("number"));
        array = data.getJSONArray("list");
        Assert.assertEquals(1, array.size());
        equals(list.get(0), array.getJSONObject(0));
    }
}
