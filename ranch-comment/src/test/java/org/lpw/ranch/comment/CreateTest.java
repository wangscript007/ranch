package org.lpw.ranch.comment;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class CreateTest extends TestSupport {
    @Test
    public void create() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/comment/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1301, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(CommentModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1302, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(CommentModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1303, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", "owner id");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1303, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        String ownerId = generator.uuid();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", generator.random(101));
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1305, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(CommentModel.NAME + ".subject"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", generator.random(101));
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1306, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(CommentModel.NAME + ".label"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1307, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(CommentModel.NAME + ".content")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("score", "-1");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1308, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(CommentModel.NAME + ".score"), 0, 5), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("score", "6");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1308, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(CommentModel.NAME + ".score"), 0, 5), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("score", "0");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1304, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.not-exists-and-not-sign-in", message.get(CommentModel.NAME + ".author")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", "author id");
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("score", "0");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1304, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.not-exists-and-not-sign-in", message.get(CommentModel.NAME + ".author")), object.getString("message"));

        mockCarousel.register("service key.get", "{\n" +
                "  \"code\":0,\n" +
                "  \"data\":{\n" +
                "    \"" + ownerId + "\":{\n" +
                "      \"id\":\"" + ownerId + "\",\n" +
                "      \"key\":\"owner key\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
        String authorId = generator.uuid();
        mockCarousel.register("ranch.user.get", "{\n" +
                "  \"code\":0,\n" +
                "  \"data\":{\n" +
                "    \"" + authorId + "\":{\n" +
                "      \"id\":\"" + authorId + "\",\n" +
                "      \"key\":\"author key\"\n" +
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
        mockHelper.getRequest().addParameter("praise", "4");
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.getRequest().addParameter("time", "2016-01-02 03:04:05");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        Assert.assertFalse(data.containsKey("key"));
        JSONObject owner = data.getJSONObject("owner");
        Assert.assertEquals(ownerId, owner.getString("id"));
        Assert.assertEquals("owner key", owner.getString("key"));
        Assert.assertFalse(data.containsKey("author"));
        Assert.assertEquals("subject", data.getString("subject"));
        Assert.assertEquals("label", data.getString("label"));
        Assert.assertEquals("content", data.getString("content"));
        Assert.assertEquals(3, data.getIntValue("score"));
        Assert.assertEquals(0, data.getIntValue("praise"));
        Assert.assertFalse(data.containsKey("audit"));
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toDate(data.getString("time"), "yyyy-MM-dd HH:mm:ss").getTime() < 2000L);
        Assert.assertFalse(data.containsKey("children"));
        CommentModel comment = liteOrm.findById(CommentModel.class, data.getString("id"));
        Assert.assertEquals("service key", comment.getKey());
        Assert.assertEquals(ownerId, comment.getOwner());
        Assert.assertEquals(authorId, comment.getAuthor());
        Assert.assertEquals("subject", comment.getSubject());
        Assert.assertEquals("label", comment.getLabel());
        Assert.assertEquals("content", comment.getContent());
        Assert.assertEquals(3, comment.getScore());
        Assert.assertEquals(0, comment.getPraise());
        Assert.assertEquals(2, comment.getAudit());
        Assert.assertTrue(System.currentTimeMillis() - comment.getTime().getTime() < 2000L);

        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "service key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("score", "3");
        mockHelper.getRequest().addParameter("praise", "4");
        mockHelper.getRequest().addParameter("audit", "1");
        mockHelper.getRequest().addParameter("time", "2016-01-02 03:04:05");
        mockHelper.mock("/comment/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        Assert.assertFalse(data.containsKey("key"));
        owner = data.getJSONObject("owner");
        Assert.assertEquals(ownerId, owner.getString("id"));
        Assert.assertEquals("owner key", owner.getString("key"));
        Assert.assertFalse(data.containsKey("author"));
        Assert.assertFalse(data.containsKey("subject"));
        Assert.assertFalse(data.containsKey("label"));
        Assert.assertEquals("content", data.getString("content"));
        Assert.assertEquals(3, data.getIntValue("score"));
        Assert.assertEquals(0, data.getIntValue("praise"));
        Assert.assertFalse(data.containsKey("audit"));
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toDate(data.getString("time"), "yyyy-MM-dd HH:mm:ss").getTime() < 2000L);
        Assert.assertFalse(data.containsKey("children"));
        comment = liteOrm.findById(CommentModel.class, data.getString("id"));
        Assert.assertEquals("service key", comment.getKey());
        Assert.assertEquals(ownerId, comment.getOwner());
        Assert.assertEquals("sign in id", comment.getAuthor());
        Assert.assertNull(comment.getSubject());
        Assert.assertNull(comment.getLabel());
        Assert.assertEquals("content", comment.getContent());
        Assert.assertEquals(3, comment.getScore());
        Assert.assertEquals(0, comment.getPraise());
        Assert.assertEquals(2, comment.getAudit());
        Assert.assertTrue(System.currentTimeMillis() - comment.getTime().getTime() < 2000L);
    }
}
