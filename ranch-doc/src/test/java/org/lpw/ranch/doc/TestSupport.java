package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditTesterDao;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.test.mock.MockScheduler;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport implements AuditTesterDao<DocModel> {
    @Autowired
    protected Message message;
    @Autowired
    protected Generator generator;
    @Autowired
    protected Converter converter;
    @Autowired
    protected Cache cache;
    @Autowired
    protected LiteOrm liteOrm;
    @Autowired
    protected Sign sign;
    @Autowired
    protected MockHelper mockHelper;
    @Autowired
    protected MockScheduler mockScheduler;
    @Autowired
    protected DocService docService;

    protected List<DocModel> create(int size) {
        List<DocModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++)
            list.add(create(i, Audit.Passed));

        return list;
    }

    public DocModel create(int i, Audit audit) {
        return create(i, "image " + i, "thumbnail " + i, "summary " + i, "label " + i, audit);
    }

    protected DocModel create(int i, String image, String thumbnail, String summary, String label, Audit audit) {
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
        liteOrm.save(doc);

        return doc;
    }

    public DocModel findById(String id) {
        return liteOrm.findById(DocModel.class, id);
    }
}
