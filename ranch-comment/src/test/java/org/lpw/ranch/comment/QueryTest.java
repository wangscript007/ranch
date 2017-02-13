package org.lpw.ranch.comment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
        Assert.assertEquals(1309, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(CommentModel.NAME + ".audit"), 0, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "3");
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1309, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(CommentModel.NAME + ".audit"), 0, 2), object.getString("message"));

        mockHelper.reset();
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockCarousel.reset();
        mockUser.register();
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
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(4, data.getIntValue("count"));
        Assert.assertEquals(20, data.getIntValue("size"));
        Assert.assertEquals(1, data.getIntValue("number"));
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(4, array.size());
        for (int i = 0; i < array.size(); i++)
            equals(list.get(3 * i), array.getJSONObject(i), 3 * i);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(3, data.getIntValue("count"));
        Assert.assertEquals(20, data.getIntValue("size"));
        Assert.assertEquals(1, data.getIntValue("number"));
        array = data.getJSONArray("list");
        Assert.assertEquals(3, array.size());
        for (int i = 0; i < array.size(); i++)
            equals(list.get(3 * i + 1), array.getJSONObject(i), 3 * i + 1);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("audit", "2");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/comment/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(3, data.getIntValue("count"));
        Assert.assertEquals(20, data.getIntValue("size"));
        Assert.assertEquals(1, data.getIntValue("number"));
        array = data.getJSONArray("list");
        Assert.assertEquals(3, array.size());
        for (int i = 0; i < array.size(); i++)
            equals(list.get(3 * i + 2), array.getJSONObject(i), 3 * i + 2);
    }

    private void equals(CommentModel comment, JSONObject obj, int i) {
        Assert.assertEquals(comment.getId(), obj.getString("id"));
        Assert.assertEquals("key " + i, obj.getString("key"));
        JSONObject owner = obj.getJSONObject("owner");
        Assert.assertEquals("owner " + i, owner.getString("id"));
        Assert.assertEquals("owner key " + i, owner.getString("key"));
        mockUser.verify(obj.getJSONObject("author"), comment.getAuthor());
        Assert.assertEquals(comment.getSubject(), obj.getString("subject"));
        Assert.assertEquals(comment.getLabel(), obj.getString("label"));
        Assert.assertEquals(comment.getContent(), obj.getString("content"));
        Assert.assertEquals(comment.getScore(), obj.getIntValue("score"));
        Assert.assertFalse(obj.containsKey("audit"));
        Assert.assertEquals(converter.toString(comment.getTime()), obj.getString("time"));
        Assert.assertFalse(obj.containsKey("children"));
    }
}
