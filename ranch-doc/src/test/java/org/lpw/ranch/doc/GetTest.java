package org.lpw.ranch.doc;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class GetTest extends TestSupport {
    @Test
    public void get() {
        DocModel doc1 = create(1, Audit.Passed);
        DocModel doc2 = create(2, null, null, null, null, Audit.Passed, Recycle.No);
        DocModel doc3 = create(3, Audit.Normal);
        DocModel doc4 = create(4, Audit.Refused);

        mockHelper.reset();
        mockHelper.mock("/doc/get");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1413, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".ids")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.mock("/doc/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());

        mockCarousel.reset();
        mockUser.register();
        for (int i = 0; i < 2; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("ids", "id," + doc1.getId() + "," + doc2.getId() + "," + doc3.getId() + "," + doc4.getId());
            mockHelper.mock("/doc/get");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getInt("code"));
            data = object.getJSONObject("data");
            Assert.assertEquals(2, data.size());
            JSONObject doc = data.getJSONObject(doc1.getId());
            Assert.assertEquals(doc1.getId(), doc.getString("id"));
            Assert.assertFalse(doc.has("key"));
            mockUser.verify(doc.getJSONObject("author"), doc1.getAuthor());
            Assert.assertFalse(doc.has("scoreMin"));
            Assert.assertFalse(doc.has("scoreMax"));
            Assert.assertFalse(doc.has("sort"));
            Assert.assertEquals("subject 1", doc.getString("subject"));
            Assert.assertEquals("image 1", doc.getString("image"));
            Assert.assertEquals("thumbnail 1", doc.getString("thumbnail"));
            Assert.assertEquals("summary 1", doc.getString("summary"));
            Assert.assertEquals("label 1", doc.getString("label"));
            Assert.assertFalse(doc.has("content"));
            Assert.assertEquals(401, doc.getInt("read"));
            Assert.assertEquals(501, doc.getInt("favorite"));
            Assert.assertEquals(601, doc.getInt("comment"));
            Assert.assertEquals(701, doc.getInt("score"));
            Assert.assertEquals(converter.toString(doc1.getTime()), doc.getString("time"));
            Assert.assertFalse(doc.has("audit"));
            doc = data.getJSONObject(doc2.getId());
            Assert.assertEquals(doc2.getId(), doc.getString("id"));
            Assert.assertFalse(doc.has("key"));
            mockUser.verify(doc.getJSONObject("author"), doc2.getAuthor());
            Assert.assertFalse(doc.has("scoreMin"));
            Assert.assertFalse(doc.has("scoreMax"));
            Assert.assertFalse(doc.has("sort"));
            Assert.assertEquals("subject 2", doc.getString("subject"));
            Assert.assertFalse(doc.has("image"));
            Assert.assertFalse(doc.has("thumbnail"));
            Assert.assertFalse(doc.has("summary"));
            Assert.assertFalse(doc.has("label"));
            Assert.assertFalse(doc.has("content"));
            Assert.assertEquals(402, doc.getInt("read"));
            Assert.assertEquals(502, doc.getInt("favorite"));
            Assert.assertEquals(602, doc.getInt("comment"));
            Assert.assertEquals(702, doc.getInt("score"));
            Assert.assertEquals(converter.toString(doc2.getTime()), doc.getString("time"));
            Assert.assertFalse(doc.has("audit"));
        }
    }
}
