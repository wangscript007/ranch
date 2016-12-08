package org.lpw.ranch.doc;

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
        mockHelper.mock("/doc/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1401, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1402, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1403, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", "owner id");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1403, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        String ownerId = generator.uuid();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1404, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".author")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", "author id");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1404, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".author")), object.getString("message"));

        mockHelper.reset();
        String authorId = generator.uuid();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1405, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".subject")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("subject", generator.random(101));
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1406, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".subject"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", generator.random(101));
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1407, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".image"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", "image");
        mockHelper.getRequest().addParameter("thumbnail", generator.random(101));
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1408, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".thumbnail"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", "image");
        mockHelper.getRequest().addParameter("thumbnail", "thumbnail");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1409, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".content")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", authorId);
        mockHelper.getRequest().addParameter("scoreMin", "1");
        mockHelper.getRequest().addParameter("scoreMax", "2");
        mockHelper.getRequest().addParameter("sort", "3");
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", "image");
        mockHelper.getRequest().addParameter("thumbnail", "thumbnail");
        mockHelper.getRequest().addParameter("summary", "summary");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("read", "4");
        mockHelper.getRequest().addParameter("favorite", "5");
        mockHelper.getRequest().addParameter("comment", "6");
        mockHelper.getRequest().addParameter("score", "7");
        mockHelper.getRequest().addParameter("audit", "8");
        mockHelper.getRequest().addParameter("time", "2016-01-02 03:04:05");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        Assert.assertEquals("key", data.getString("key"));
        Assert.assertEquals(ownerId, data.getString("owner"));
        Assert.assertEquals(authorId, data.getString("author"));
        Assert.assertEquals(1, data.getInt("scoreMin"));
        Assert.assertEquals(2, data.getInt("scoreMax"));
        Assert.assertEquals(3, data.getInt("sort"));
        Assert.assertEquals("subject", data.getString("subject"));
        Assert.assertEquals("image", data.getString("image"));
        Assert.assertEquals("thumbnail", data.getString("thumbnail"));
        Assert.assertEquals("label", data.getString("label"));
        Assert.assertFalse(data.has("content"));
        Assert.assertEquals(0, data.getInt("read"));
        Assert.assertEquals(0, data.getInt("favorite"));
        Assert.assertEquals(0, data.getInt("comment"));
        Assert.assertEquals(0, data.getInt("score"));
        Assert.assertEquals(2, data.getInt("audit"));
        Assert.assertTrue(System.currentTimeMillis() - converter.toDate(data.getString("time"), "yyyy-MM-dd HH:mm:ss").getTime() < 2000L);
    }
}
