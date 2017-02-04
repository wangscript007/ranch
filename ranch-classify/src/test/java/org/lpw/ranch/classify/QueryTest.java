package org.lpw.ranch.classify;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Test
    public void query() {
        for (int i = 0; i < 20; i++) {
            ClassifyModel classify = new ClassifyModel();
            classify.setCode("code " + converter.toString(i, "00"));
            classify.setName("name " + converter.toString(i, "00"));
            classify.setRecycle((i % 2 == 0 ? Recycle.No : Recycle.Yes).getValue());
            liteOrm.save(classify);
        }

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/classify/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(10, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        JSONArray array = data.getJSONArray("list");
        for (int i = 0, size = array.size(); i < size; i++)
            equalsCodeName(array.getJSONObject(i), "code " + converter.toString(2 * i, "00"), "name " + converter.toString(2 * i, "00"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(5, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        array = data.getJSONArray("list");
        for (int i = 0; i < 5; i++)
            equalsCodeName(array.getJSONObject(i), "code " + (10 + 2 * i), "name " + (10 + 2 * i));
    }
}
