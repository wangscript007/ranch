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
public class QueryByAuthorTest extends TestSupport {
    @Test
    public void queryByAuthor() {
        clean();
        String[] authors = new String[]{generator.uuid(), generator.uuid()};
        List<CommentModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            list.add(create(i, "owner " + i, authors[i % authors.length], Audit.values()[i % 3]));

        mockHelper.reset();
        mockHelper.mock("/comment/query-by-author");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1304, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".author")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("author", "author id");
        mockHelper.mock("/comment/query-by-author");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1304, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".author")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("author", authors[0]);
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/comment/query-by-author");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(10, data.getInt("count"));
        Assert.assertEquals(20, data.getInt("size"));
        Assert.assertEquals(1, data.getInt("number"));
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(10, array.size());
        for (int i = 0; i < array.size(); i++)
            Assert.assertEquals(list.get(2 * i).getId(), array.getJSONObject(i).getString("id"));
    }
}
