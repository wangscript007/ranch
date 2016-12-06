package org.lpw.ranch.comment;

import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Autowired
    protected Message message;
    @Autowired
    protected Generator generator;
    @Autowired
    protected Converter converter;
    @Autowired
    protected LiteOrm liteOrm;
    @Autowired
    protected Request request;
    @Autowired
    protected MockHelper mockHelper;

    protected void clean() {
        liteOrm.delete(new LiteQuery(CommentModel.class), null);
    }

    protected CommentModel create(int i, Audit audit) {
        return create(i, "owner " + i, "author " + i, audit);
    }

    protected CommentModel create(int i, String owner, String author, Audit audit) {
        CommentModel comment = new CommentModel();
        comment.setKey("key " + i);
        comment.setOwner(owner);
        comment.setAuthor(author);
        comment.setSubject("subject " + i);
        comment.setLabel("label " + i);
        comment.setContent("content " + i);
        comment.setScore(i);
        comment.setTime(new Timestamp(System.currentTimeMillis() - i * TimeUnit.Hour.getTime()));
        comment.setAudit(audit.getValue());
        liteOrm.save(comment);

        return comment;
    }

    protected CommentModel findById(String id) {
        return liteOrm.findById(CommentModel.class, id);
    }
}
