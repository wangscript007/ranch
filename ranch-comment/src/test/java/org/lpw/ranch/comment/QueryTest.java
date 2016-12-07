package org.lpw.ranch.comment;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Test
    public void query() {
        List<CommentModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(create(i, Audit.values()[i % 3]));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "-1");
        mockHelper.mock("/comment/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1309, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(CommentModel.NAME + ".audit"), 0, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "3");
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1309, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(CommentModel.NAME + ".audit"), 0, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1391, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(4, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(4, array.size());
        for (int i = 0; i < array.size(); i++)
            Assert.assertEquals(list.get(3 * i).getId(), array.getJSONObject(i).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(3, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        array = data.getJSONArray("list");
        Assert.assertEquals(3, array.size());
        for (int i = 0; i < array.size(); i++)
            Assert.assertEquals(list.get(3 * i + 1).getId(), array.getJSONObject(i).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(3, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        array = data.getJSONArray("list");
        Assert.assertEquals(3, array.size());
        for (int i = 0; i < array.size(); i++)
            Assert.assertEquals(list.get(3 * i + 2).getId(), array.getJSONObject(i).getString("id"));
    }
}
