package org.lpw.ranch.alipay;

import com.alibaba.fastjson.JSONArray;
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
        mockHelper.reset();
        mockHelper.mock("/alipay/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/alipay/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertTrue(data.isEmpty());

        List<AlipayModel> list = new ArrayList<>();
        list.add(create(0));
        mockHelper.reset();
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/alipay/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        equals(data.getJSONObject(0), 0);

        list.add(create(1));
        mockHelper.reset();
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/alipay/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(2, data.size());
        JSONObject obj = data.getJSONObject(0);
        if (list.get(0).getId().equals(obj.getString("id"))) {
            equals(data.getJSONObject(0), 0);
            equals(data.getJSONObject(1), 1);
        } else {
            equals(data.getJSONObject(0), 1);
            equals(data.getJSONObject(1), 0);
        }
    }
}
