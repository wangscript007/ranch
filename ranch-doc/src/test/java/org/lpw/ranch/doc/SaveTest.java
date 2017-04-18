package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.TimeUnit;

/**
 * @author lpw
 */
public class SaveTest extends TestSupport {
    @Test
    public void save() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/doc/save");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1401, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/doc/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1402, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", "owner id");
        mockHelper.mock("/doc/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1403, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".owner")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.mock("/doc/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1405, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".subject")), object.getString("message"));

        String ownerId = generator.uuid();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", generator.random(101));
        mockHelper.mock("/doc/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1406, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".subject"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", generator.random(101));
        mockHelper.mock("/doc/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1407, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".image"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", "image");
        mockHelper.getRequest().addParameter("thumbnail", generator.random(101));
        mockHelper.mock("/doc/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1408, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(DocModel.NAME + ".thumbnail"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", "image");
        mockHelper.getRequest().addParameter("thumbnail", "thumbnail");
        mockHelper.mock("/doc/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1409, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".source")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("source", "source");
        mockHelper.mock("/doc/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("key.get", "{\n" +
                "  \"code\":0,\n" +
                "  \"data\":{\n" +
                "    \"" + ownerId + "\":{\n" +
                "      \"id\":\"" + ownerId + "\",\n" +
                "      \"key\":\"owner key\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("owner", ownerId);
        mockHelper.getRequest().addParameter("author", "author id");
        mockHelper.getRequest().addParameter("scoreMin", "1");
        mockHelper.getRequest().addParameter("scoreMax", "2");
        mockHelper.getRequest().addParameter("sort", "3");
        mockHelper.getRequest().addParameter("subject", "subject");
        mockHelper.getRequest().addParameter("image", "image");
        mockHelper.getRequest().addParameter("thumbnail", "thumbnail");
        mockHelper.getRequest().addParameter("summary", "summary");
        mockHelper.getRequest().addParameter("label", "label");
        mockHelper.getRequest().addParameter("source", "# source");
        mockHelper.getRequest().addParameter("content", "content");
        mockHelper.getRequest().addParameter("read", "4");
        mockHelper.getRequest().addParameter("favorite", "5");
        mockHelper.getRequest().addParameter("comment", "6");
        mockHelper.getRequest().addParameter("score", "7");
        mockHelper.getRequest().addParameter("audit", "8");
        mockHelper.getRequest().addParameter("time", "2016-01-02 03:04:05");
        mockHelper.mock("/doc/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        Assert.assertEquals("key", data.getString("key"));
        JSONObject owner = data.getJSONObject("owner");
        Assert.assertEquals(ownerId, owner.getString("id"));
        Assert.assertEquals("owner key", owner.getString("key"));
        JSONObject author = data.getJSONObject("author");
        Assert.assertEquals("sign in id", author.getString("id"));
        Assert.assertEquals(1, data.getIntValue("scoreMin"));
        Assert.assertEquals(2, data.getIntValue("scoreMax"));
        Assert.assertEquals(3, data.getIntValue("sort"));
        Assert.assertEquals("subject", data.getString("subject"));
        Assert.assertEquals("image", data.getString("image"));
        Assert.assertEquals("thumbnail", data.getString("thumbnail"));
        Assert.assertEquals("label", data.getString("label"));
        Assert.assertFalse(data.containsKey("source"));
        Assert.assertFalse(data.containsKey("content"));
        Assert.assertEquals(0, data.getIntValue("read"));
        Assert.assertEquals(0, data.getIntValue("favorite"));
        Assert.assertEquals(0, data.getIntValue("comment"));
        Assert.assertEquals(0, data.getIntValue("score"));
        Assert.assertEquals(2, data.getIntValue("audit"));
        Assert.assertTrue(time - dateTime.toTime(data.getString("time")).getTime() < 2000L);
        DocModel doc1 = findById(data.getString("id"));
        Assert.assertEquals("key", doc1.getKey());
        Assert.assertEquals(ownerId, doc1.getOwner());
        Assert.assertEquals("sign in id", doc1.getAuthor());
        Assert.assertEquals(1, doc1.getScoreMin());
        Assert.assertEquals(2, doc1.getScoreMax());
        Assert.assertEquals(3, doc1.getSort());
        Assert.assertEquals("subject", doc1.getSubject());
        Assert.assertEquals("image", doc1.getImage());
        Assert.assertEquals("thumbnail", doc1.getThumbnail());
        Assert.assertEquals("label", doc1.getLabel());
        Assert.assertEquals("# source", doc1.getSource());
        Assert.assertEquals("# source", doc1.getContent());
        Assert.assertEquals(0, doc1.getRead());
        Assert.assertEquals(0, doc1.getFavorite());
        Assert.assertEquals(0, doc1.getComment());
        Assert.assertEquals(0, doc1.getScore());
        Assert.assertEquals(2, doc1.getAudit());
        Assert.assertTrue(time - doc1.getTime().getTime() < 2000L);

        thread.sleep(3, TimeUnit.Second);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", doc1.getId());
        mockHelper.getRequest().addParameter("key", "key 2");
        mockHelper.getRequest().addParameter("author", "author id 2");
        mockHelper.getRequest().addParameter("scoreMin", "11");
        mockHelper.getRequest().addParameter("scoreMax", "22");
        mockHelper.getRequest().addParameter("sort", "33");
        mockHelper.getRequest().addParameter("subject", "subject 2");
        mockHelper.getRequest().addParameter("image", "image 2");
        mockHelper.getRequest().addParameter("thumbnail", "thumbnail 2");
        mockHelper.getRequest().addParameter("summary", "summary 2");
        mockHelper.getRequest().addParameter("label", "label 2");
        mockHelper.getRequest().addParameter("source", "# source 2");
        mockHelper.getRequest().addParameter("content", "content 2");
        mockHelper.getRequest().addParameter("read", "44");
        mockHelper.getRequest().addParameter("favorite", "55");
        mockHelper.getRequest().addParameter("comment", "66");
        mockHelper.getRequest().addParameter("score", "77");
        mockHelper.getRequest().addParameter("audit", "88");
        mockHelper.getRequest().addParameter("time", "2016-01-02 33:44:55");
        mockHelper.getRequest().addParameter("markdown", "true");
        mockHelper.mock("/doc/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(doc1.getId(), data.getString("id"));
        Assert.assertEquals("key 2", data.getString("key"));
        owner = data.getJSONObject("owner");
        Assert.assertEquals(1, owner.size());
        Assert.assertEquals("", owner.getString("id"));
        author = data.getJSONObject("author");
        Assert.assertEquals("sign in id", author.getString("id"));
        Assert.assertEquals(11, data.getIntValue("scoreMin"));
        Assert.assertEquals(22, data.getIntValue("scoreMax"));
        Assert.assertEquals(33, data.getIntValue("sort"));
        Assert.assertEquals("subject 2", data.getString("subject"));
        Assert.assertEquals("image 2", data.getString("image"));
        Assert.assertEquals("thumbnail 2", data.getString("thumbnail"));
        Assert.assertEquals("label 2", data.getString("label"));
        Assert.assertFalse(data.containsKey("source"));
        Assert.assertFalse(data.containsKey("content"));
        Assert.assertEquals(0, data.getIntValue("read"));
        Assert.assertEquals(0, data.getIntValue("favorite"));
        Assert.assertEquals(0, data.getIntValue("comment"));
        Assert.assertEquals(0, data.getIntValue("score"));
        Assert.assertEquals(2, data.getIntValue("audit"));
        Assert.assertTrue(time - dateTime.toTime(data.getString("time")).getTime() < 2000L);
        DocModel doc2 = findById(data.getString("id"));
        Assert.assertEquals("key 2", doc2.getKey());
        Assert.assertEquals("", doc2.getOwner());
        Assert.assertEquals("sign in id", doc2.getAuthor());
        Assert.assertEquals(11, doc2.getScoreMin());
        Assert.assertEquals(22, doc2.getScoreMax());
        Assert.assertEquals(33, doc2.getSort());
        Assert.assertEquals("subject 2", doc2.getSubject());
        Assert.assertEquals("image 2", doc2.getImage());
        Assert.assertEquals("thumbnail 2", doc2.getThumbnail());
        Assert.assertEquals("label 2", doc2.getLabel());
        Assert.assertEquals("# source 2", doc2.getSource());
        Assert.assertEquals("<h1>source 2</h1>", doc2.getContent());
        Assert.assertEquals(0, doc2.getRead());
        Assert.assertEquals(0, doc2.getFavorite());
        Assert.assertEquals(0, doc2.getComment());
        Assert.assertEquals(0, doc2.getScore());
        Assert.assertEquals(2, doc2.getAudit());
        Assert.assertTrue(time - doc2.getTime().getTime() < 2000L);
    }
}
