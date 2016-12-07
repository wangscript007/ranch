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
public class QueryByOwnerTest extends TestSupport {
    @Test
    public void queryByOwner() {
        String[] owners = new String[]{generator.uuid(), generator.uuid()};
        List<CommentModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            list.add(create(i, owners[i % owners.length], "author " + i, Audit.values()[i % 3]));

        mockHelper.reset();
        mockHelper.mock("/comment/query-by-owner");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1303, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("owner", "owner id");
        mockHelper.mock("/comment/query-by-owner");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1303, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("owner", owners[0]);
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/comment/query-by-owner");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(3, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(3, array.size());
        for (int i = 0; i < array.size(); i++)
            Assert.assertEquals(list.get(6 * (i + 1) - 2).getId(), array.getJSONObject(i).getString("id"));

        for (Audit audit : Audit.values()) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("owner", owners[1]);
            mockHelper.getRequest().addParameter("audit", "" + audit.getValue());
            mockHelper.getRequest().addParameter("pageSize", "20");
            mockHelper.getRequest().addParameter("pageNum", "0");
            request.putSign(mockHelper.getRequest().getMap());
            mockHelper.mock("/comment/query-by-owner");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getInt("code"));
            data = object.getJSONObject("data");
            Assert.assertEquals(4, data.getInt("count"));
            Assert.assertEquals(20, data.getInt("size"));
            Assert.assertEquals(1, data.getInt("number"));
            array = data.getJSONArray("list");
            Assert.assertEquals(4, array.size());
            for (int i = 0; i < array.size(); i++)
                Assert.assertEquals(list.get(6 * (i + 1) - 5).getId(), array.getJSONObject(i).getString("id"));
        }
    }
}
