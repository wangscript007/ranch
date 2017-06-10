package org.lpw.ranch.comment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.recycle.Recycle;
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
            list.add(create(i, owners[i % owners.length], "author " + i, Audit.values()[i % 3], Recycle.No));
        CommentModel child = create(101, list.get(1).getId(), "author 1", Audit.Pass, Recycle.No);

        mockHelper.reset();
        mockHelper.mock("/comment/query-by-owner");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1303, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("owner", "owner id");
        mockHelper.mock("/comment/query-by-owner");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1303, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".owner")), object.getString("message"));

        mockCarousel.reset();
        mockUser.register();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("owner", owners[0]);
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query-by-owner");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(3, 20, 1, data);
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(3, array.size());
        for (int i = 0; i < array.size(); i++)
            equals(list.get(6 * (i + 1) - 2), array.getJSONObject(i), 6 * (i + 1) - 2, child);

        for (Audit audit : Audit.values()) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("owner", owners[1]);
            mockHelper.getRequest().addParameter("audit", "" + audit.getValue());
            mockHelper.getRequest().addParameter("pageSize", "20");
            mockHelper.getRequest().addParameter("pageNum", "0");
            sign.put(mockHelper.getRequest().getMap(), null);
            mockHelper.mock("/comment/query-by-owner");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            data = object.getJSONObject("data");
            pageTester.assertCountSizeNumber(4, 20, 1, data);
            array = data.getJSONArray("list");
            Assert.assertEquals(4, array.size());
            for (int i = 0; i < array.size(); i++)
                equals(list.get(6 * (i + 1) - 5), array.getJSONObject(i), 6 * (i + 1) - 5, child);
        }
    }

    private void equals(CommentModel comment, JSONObject obj, int i, CommentModel child) {
        Assert.assertEquals(comment.getId(), obj.getString("id"));
        Assert.assertFalse(obj.containsKey("key"));
        Assert.assertFalse(obj.containsKey("owner"));
        mockUser.verify(obj.getJSONObject("author"), comment.getAuthor());
        Assert.assertEquals(comment.getSubject(), obj.getString("subject"));
        Assert.assertEquals(comment.getLabel(), obj.getString("label"));
        Assert.assertEquals(comment.getContent(), obj.getString("content"));
        Assert.assertEquals(comment.getScore(), obj.getIntValue("score"));
        Assert.assertFalse(obj.containsKey("audit"));
        Assert.assertEquals(converter.toString(comment.getTime()), obj.getString("time"));
        if (i == 1) {
            JSONArray children = obj.getJSONArray("children");
            Assert.assertEquals(1, children.size());
            equals(child, children.getJSONObject(0), 101, null);
        } else
            Assert.assertFalse(obj.containsKey("children"));
    }
}
