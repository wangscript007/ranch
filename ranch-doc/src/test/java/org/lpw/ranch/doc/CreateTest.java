package org.lpw.ranch.doc;

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
        mockHelper.reset();
        mockHelper.mock("/doc/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1401, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1402, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1403, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", "owner id");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1403, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".owner")), object.getString("message"));

        String ownerId=generator.uuid();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1405, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".subject")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", generator.random(101));
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1406, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".subject"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", generator.random(101));
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1407, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".image"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", "image");
        mockHelper.getRequest().addParameter("thumbnail", generator.random(101));
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1408, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".thumbnail"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", "image");
        mockHelper.getRequest().addParameter("thumbnail", "thumbnail");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1409, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".source")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", "image");
        mockHelper.getRequest().addParameter("thumbnail", "thumbnail");
        mockHelper.getRequest().addParameter("source", "source");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1410, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".content")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockUser.register();
        mockCarousel.register("key.get", "{\n" +
                "  \"code\":0,\n" +
                "  \"data\":{\n" +
                "    \"" + ownerId + "\":{\n" +
                "      \"id\":\"" + ownerId + "\",\n" +
                "      \"key\":\"owner key\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("scoreMin", "1");
        mockHelper.getRequest().addParameter("scoreMax", "2");
        mockHelper.getRequest().addParameter("sort", "3");
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", "image");
        mockHelper.getRequest().addParameter("thumbnail", "thumbnail");
        mockHelper.getRequest().addParameter("summary", "summary");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.getRequest().addParameter("source", "source");
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("read", "4");
        mockHelper.getRequest().addParameter("favorite", "5");
        mockHelper.getRequest().addParameter("comment", "6");
        mockHelper.getRequest().addParameter("score", "7");
        mockHelper.getRequest().addParameter("audit", "8");
        mockHelper.getRequest().addParameter("time", "2016-01-02 03:04:05");
        mockHelper.mock("/doc/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        Assert.assertEquals("key", data.getString("key"));
        JSONObject owner = data.getJSONObject("owner");
        Assert.assertEquals(ownerId, owner.getString("id"));
        Assert.assertEquals("owner key", owner.getString("key"));
        Assert.assertEquals(1, data.getIntValue("scoreMin"));
        Assert.assertEquals(2, data.getIntValue("scoreMax"));
        Assert.assertEquals(3, data.getIntValue("sort"));
        Assert.assertEquals("subject", data.getString("subject"));
        Assert.assertEquals("image", data.getString("image"));
        Assert.assertEquals("thumbnail", data.getString("thumbnail"));
        Assert.assertEquals("label", data.getString("label"));
        Assert.assertEquals("source", data.getString("source"));
        Assert.assertEquals("content", data.getString("content"));
        Assert.assertEquals(0, data.getIntValue("read"));
        Assert.assertEquals(0, data.getIntValue("favorite"));
        Assert.assertEquals(0, data.getIntValue("comment"));
        Assert.assertEquals(0, data.getIntValue("score"));
        Assert.assertEquals(2, data.getIntValue("audit"));
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toDate(data.getString("time"), "yyyy-MM-dd HH:mm:ss").getTime() < 2000L);
    }
}
