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
public class QueryByAuthorTest extends TestSupport {
    @Test
    public void queryByAuthor() {
        String[] authors = new String[]{generator.uuid(), generator.uuid()};
        List<CommentModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            list.add(create(i, "owner " + i, authors[i % authors.length], Audit.values()[i % 3], Recycle.No));

        mockHelper.reset();
        mockHelper.mock("/comment/query-by-author");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1304, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".author")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("author", "author id");
        mockHelper.mock("/comment/query-by-author");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1304, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".author")), object.getString("message"));

        mockCarousel.reset();
        for (int i = 0; i < list.size(); i++) {
            mockCarousel.register("key " + i + ".get", "{\n" +
                    "  \"code\":0,\n" +
                    "  \"data\":{\n" +
                    "    \"owner " + i + "\":{\n" +
                    "      \"key\":\"owner key " + i + "\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
        }
        mockHelper.reset();
        mockHelper.getRequest().addParameter("author", authors[0]);
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        mockHelper.mock("/comment/query-by-author");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(10, data.getIntValue("count"));
        Assert.assertEquals(20, data.getIntValue("size"));
        Assert.assertEquals(1, data.getIntValue("number"));
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(10, array.size());
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            CommentModel comment = list.get(2 * i);
            Assert.assertEquals(comment.getId(), obj.getString("id"));
            Assert.assertFalse(obj.containsKey("key"));
            JSONObject owner = obj.getJSONObject("owner");
            Assert.assertEquals("owner " + (i * 2), owner.getString("id"));
            Assert.assertEquals("owner key " + (i * 2), owner.getString("key"));
            Assert.assertFalse(obj.containsKey("author"));
            Assert.assertEquals(comment.getSubject(), obj.getString("subject"));
            Assert.assertEquals(comment.getLabel(), obj.getString("label"));
            Assert.assertEquals(comment.getContent(), obj.getString("content"));
            Assert.assertEquals(comment.getScore(), obj.getIntValue("score"));
            Assert.assertFalse(obj.containsKey("audit"));
            Assert.assertEquals(converter.toString(comment.getTime()), obj.getString("time"));
            Assert.assertFalse(obj.containsKey("children"));
        }
    }
}
