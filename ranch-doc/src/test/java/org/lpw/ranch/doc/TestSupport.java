package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditTesterDao;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.ranch.user.MockUser;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.SchedulerAspect;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
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
    Converter converter;
    @Inject
    Cache cache;
    @Inject
    LiteOrm liteOrm;
    @Inject
    Sign sign;
    @Inject
    SchedulerAspect schedulerAspect;
    @Inject
    MockHelper mockHelper;
    @Inject
    MockCarousel mockCarousel;
    @Inject
    DocService docService;
    @Inject
    MockUser mockUser;

    List<DocModel> create(int size) {
        List<DocModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(create(i, Audit.Passed));

        return list;
    }

    @Override
    public DocModel create(int i, Recycle recycle) {
        return create(i, "image " + i, "thumbnail " + i, "summary " + i, "label " + i, Audit.Normal, recycle);
    }

    @Override
    public DocModel create(int i, Audit audit) {
        return create(i, "image " + i, "thumbnail " + i, "summary " + i, "label " + i, audit, Recycle.No);
    }

    DocModel create(int i, String image, String thumbnail, String summary, String label, Audit audit, Recycle recycle) {
        DocModel doc = new DocModel();
        doc.setKey("key " + i);
        doc.setOwner("owner " + i);
        doc.setAuthor("author " + i);
        doc.setScoreMin(100 + i);
        doc.setScoreMax(200 + i);
        doc.setSort(300 + i);
        doc.setSubject("subject " + i);
        doc.setImage(image);
        doc.setThumbnail(thumbnail);
        doc.setSummary(summary);
        doc.setLabel(label);
        doc.setContent("content " + i);
        doc.setRead(400 + i);
        doc.setFavorite(500 + i);
        doc.setComment(600 + i);
        doc.setScore(700 + i);
        doc.setTime(new Timestamp(System.currentTimeMillis() - i * TimeUnit.Day.getTime()));
        doc.setAudit(audit.getValue());
        doc.setAuditRemark("remark " + i);
        doc.setRecycle(recycle.getValue());
        liteOrm.save(doc);

        return doc;
    }

    public DocModel findById(String id) {
        return liteOrm.findById(DocModel.class, id);
    }

    @Override
    public void clean() {
        liteOrm.delete(new LiteQuery(DocModel.class), null);
    }
}
