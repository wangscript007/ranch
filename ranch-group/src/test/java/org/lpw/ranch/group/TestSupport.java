package org.lpw.ranch.group;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    Message message;
    @Inject
    Generator generator;
    @Inject
    Converter converter;
    @Inject
    LiteOrm liteOrm;
    @Inject
    MockCarousel mockCarousel;
    @Inject
    MockHelper mockHelper;
    long now = System.currentTimeMillis();

    GroupModel create(int i) {
        GroupModel group = new GroupModel();
        group.setOwner("owner " + i);
        group.setName("name " + i);
        group.setNote("note " + i);
        group.setMember(100 + i);
        group.setAudit(i);
        group.setCreate(new Timestamp(now - i * TimeUnit.Day.getTime()));
        liteOrm.save(group);

        return group;
    }
}
