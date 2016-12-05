package org.lpw.ranch.classify;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteQuery;

/**
 * @author lpw
 */
public class RecycleTest extends TestSupport{
    @Test
    public void recycle() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
        for (int i = 0; i < 20; i++)
            create(10 + i, i % 2 == 0);

        mockHelper.reset();
        mockHelper.mock("/classify/recycle");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "1");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/recycle");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(10, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        JSONArray array = data.getJSONArray("list");
        for (int i = 0, size = array.size(); i < size; i++)
            equalsCodeName(array.getJSONObject(i), "code " + (10 + 2 * i), "name " + (10 + 2 * i));
    }
}
