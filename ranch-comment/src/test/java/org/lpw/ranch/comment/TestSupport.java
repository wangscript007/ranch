package org.lpw.ranch.comment;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditTesterDao;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.ranch.user.MockUser;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.PageTester;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport implements AuditTesterDao<CommentModel> {
    @Inject
    Message message;
    @Inject
    Generator generator;
    @Inject
    Converter converter;
    @Inject
    DateTime dateTime;
    @Inject
    LiteOrm liteOrm;
    @Inject
    Sign sign;
    @Inject
    PageTester pageTester;
    @Inject
    MockHelper mockHelper;
    @Inject
    MockCarousel mockCarousel;
    @Inject
    MockUser mockUser;
    long time = System.currentTimeMillis();

    @Override
    public CommentModel create(int i, Recycle recycle) {
        return create(i, "owner " + i, "author " + i, Audit.Normal, recycle);
    }

    @Override
    public CommentModel create(int i, Audit audit) {
        return create(i, "owner " + i, "author " + i, audit, Recycle.No);
    }

    CommentModel create(int i, String owner, String author, Audit audit, Recycle recycle) {
        CommentModel comment = new CommentModel();
        comment.setKey("key " + i);
        comment.setOwner(owner);
        comment.setAuthor(author);
        comment.setSubject("subject " + i);
        comment.setLabel("label " + i);
        comment.setContent("content " + i);
        comment.setScore(i);
        comment.setPraise(10 + i);
        comment.setTime(new Timestamp(time - i * TimeUnit.Day.getTime()));
        comment.setAudit(audit.getValue());
        comment.setAuditRemark("remark " + i);
        comment.setRecycle(recycle.getValue());
        liteOrm.save(comment);

        return comment;
    }

    void equals(CommentModel comment, JSONObject obj, int i) {
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

    public CommentModel findById(String id) {
        return liteOrm.findById(CommentModel.class, id);
    }

    @Override
    public void clear() {
        liteOrm.delete(new LiteQuery(CommentModel.class), null);
    }
}
