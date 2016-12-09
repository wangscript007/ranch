package org.lpw.ranch.comment;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class CreateTest extends TestSupport {
    @Test
    public void create() {
        mockHelper.reset();
        mockHelper.mock("/comment/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1301, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(CommentModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1302, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(CommentModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1303, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", "owner id");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1303, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        String ownerId = generator.uuid();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1304, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".author")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", "author id");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1304, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".author")), object.getString("message"));

        mockHelper.reset();
        String authorId = generator.uuid();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("subject", generator.random(101));
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1305, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(CommentModel.NAME + ".subject"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", generator.random(101));
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1306, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(CommentModel.NAME + ".label"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1307, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(CommentModel.NAME + ".content")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("score", "-1");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1308, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(CommentModel.NAME + ".score"), 0, 5), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("score", "6");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1308, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(CommentModel.NAME + ".score"), 0, 5), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("service key.get", "{\n" +
                "  \"code\":0,\n" +
                "  \"data\":{\n" +
                "    \"" + ownerId + "\":{\n" +
                "      \"key\":\"owner key\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("score", "3");
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.getRequest().addParameter("time", "2016-01-02 03:04:05");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        Assert.assertFalse(data.has("key"));
        JSONObject owner = data.getJSONObject("owner");
        Assert.assertEquals(ownerId, owner.getString("id"));
        Assert.assertEquals("owner key", owner.getString("key"));
        Assert.assertFalse(data.has("author"));
        Assert.assertEquals("subject", data.getString("subject"));
        Assert.assertEquals("label", data.getString("label"));
        Assert.assertEquals("content", data.getString("content"));
        Assert.assertEquals(3, data.getInt("score"));
        Assert.assertFalse(data.has("audit"));
        Assert.assertTrue(System.currentTimeMillis() - converter.toDate(data.getString("time"), "yyyy-MM-dd HH:mm:ss").getTime() < 2000L);
        Assert.assertFalse(data.has("children"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("score", "3");
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.getRequest().addParameter("time", "2016-01-02 03:04:05");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        Assert.assertFalse(data.has("key"));
        owner = data.getJSONObject("owner");
        Assert.assertEquals(ownerId, owner.getString("id"));
        Assert.assertEquals("owner key", owner.getString("key"));
        Assert.assertFalse(data.has("author"));
        Assert.assertFalse(data.has("subject"));
        Assert.assertFalse(data.has("label"));
        Assert.assertEquals("content", data.getString("content"));
        Assert.assertEquals(3, data.getInt("score"));
        Assert.assertFalse(data.has("audit"));
        Assert.assertTrue(System.currentTimeMillis() - converter.toDate(data.getString("time"), "yyyy-MM-dd HH:mm:ss").getTime() < 2000L);
        Assert.assertFalse(data.has("children"));
    }
}
