package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditTesterDao;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.PageTester;
import org.lpw.tephra.test.SchedulerAspect;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Thread;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport implements AuditTesterDao<DocModel> {
    @Inject
    Message message;
    @Inject
    Generator generator;
    @Inject
    DateTime dateTime;
    @Inject
    Thread thread;
    @Inject
    LiteOrm liteOrm;
    @Inject
    Sign sign;
    @Inject
    SchedulerAspect schedulerAspect;
    @Inject
    PageTester pageTester;
    @Inject
    MockHelper mockHelper;
    @Inject
    MockCarousel mockCarousel;
    @Inject
    DocService docService;
    long time = System.currentTimeMillis() / 60 / 1000 * 60 * 1000;

    List<DocModel> create(int size) {
        List<DocModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(create(i, Audit.Pass));

        return list;
    }

    @Override
    public DocModel create(int i, Recycle recycle) {
        return create(i, "author " + i, "image " + i, "thumbnail " + i, "summary " + i, "label " + i, Audit.Normal, recycle);
    }

    @Override
    public DocModel create(int i, Audit audit) {
        return create(i, "image " + i, "thumbnail " + i, "summary " + i, "label " + i, audit, Recycle.No);
    }

    DocModel create(int i, String author, Audit audit) {
        return create(i, author, "image " + i, "thumbnail " + i, "summary " + i, "label " + i, audit, Recycle.No);
    }

    DocModel create(int i, String image, String thumbnail, String summary, String label, Audit audit, Recycle recycle) {
        return create(i, "author " + i, image, thumbnail, summary, label, audit, recycle);
    }

    private DocModel create(int i, String author, String image, String thumbnail, String summary, String label, Audit audit, Recycle recycle) {
        DocModel doc = new DocModel();
        doc.setKey("key " + i);
        doc.setAuthor(author);
        doc.setSort(300 + i);
        doc.setSubject("subject " + i);
        doc.setImage(image);
        doc.setThumbnail(thumbnail);
        doc.setSummary(summary);
        doc.setLabel(label);
        doc.setSource("source " + i);
        doc.setContent("content " + i);
        doc.setRead(400 + i);
        doc.setFavorite(500 + i);
        doc.setComment(600 + i);
        doc.setPraise(800 + i);
        doc.setScore(700 + i);
        doc.setTime(new Timestamp(time - i * TimeUnit.Day.getTime()));
        doc.setAudit(audit.getValue());
        doc.setAuditRemark("remark " + i);
        doc.setRecycle(recycle.getValue());
        liteOrm.save(doc);

        return doc;
    }

    void equals(JSONObject object, int i, Audit audit) {
        equals(object, i, "author " + i, audit);
    }

    void equals(JSONObject object, int i, String author, Audit audit) {
        Assert.assertEquals(21, object.size());
        Assert.assertEquals(36, object.getString("id").length());
        Assert.assertEquals("key " + i, object.getString("key"));
        JSONObject owner = object.getJSONObject("owner");
        Assert.assertEquals(1, owner.size());
        Assert.assertEquals("owner " + i, owner.getString("id"));
        JSONObject auth = object.getJSONObject("author");
        Assert.assertEquals(1, auth.size());
        Assert.assertEquals(author, auth.getString("id"));
        Assert.assertEquals(100 + i, object.getIntValue("scoreMin"));
        Assert.assertEquals(200 + i, object.getIntValue("scoreMax"));
        Assert.assertEquals(300 + i, object.getIntValue("sort"));
        Assert.assertEquals("subject " + i, object.getString("subject"));
        Assert.assertEquals("image " + i, object.getString("image"));
        Assert.assertEquals("thumbnail " + i, object.getString("thumbnail"));
        Assert.assertEquals("summary " + i, object.getString("summary"));
        Assert.assertEquals("label " + i, object.getString("label"));
        Assert.assertEquals("source " + i, object.getString("source"));
        Assert.assertEquals(400 + i, object.getIntValue("read"));
        Assert.assertEquals(500 + i, object.getIntValue("favorite"));
        Assert.assertEquals(600 + i, object.getIntValue("comment"));
        Assert.assertEquals(800 + i, object.getIntValue("praise"));
        Assert.assertEquals(700 + i, object.getIntValue("score"));
        Assert.assertEquals(dateTime.toString(new Timestamp(time - i * TimeUnit.Day.getTime())), object.getString("time"));
        Assert.assertEquals(audit.getValue(), object.getIntValue("audit"));
        Assert.assertEquals("remark " + i, object.getString("auditRemark"));
    }

    @Override
    public DocModel findById(String id) {
        return liteOrm.findById(DocModel.class, id);
    }

    @Override
    public void clear() {
        liteOrm.delete(new LiteQuery(DocModel.class), null);
    }
}
