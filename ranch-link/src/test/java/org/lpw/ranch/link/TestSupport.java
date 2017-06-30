package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.DateTime;
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
    DateTime dateTime;
    @Inject
    MockHelper mockHelper;
    @Inject
    LiteOrm liteOrm;
    private long time = System.currentTimeMillis() / 1000 * 1000;

    LinkModel create(int i) {
        return create(i, i, i, i);
    }

    LinkModel create(int i, int type, int i1, int i2) {
        LinkModel link = new LinkModel();
        link.setType("type " + type);
        link.setId1("id1 " + i1);
        link.setId2("id2 " + i2);
        link.setJson("{\"label\":\"label " + i + "\"}");
        link.setTime(new Timestamp(time - i * TimeUnit.Hour.getTime()));
        liteOrm.save(link);

        return link;
    }

    void equals(JSONObject object, int i, int type, int i1, int i2) {
        Assert.assertEquals(5, object.size());
        Assert.assertEquals("type " + type, object.getString("type"));
        Assert.assertEquals("id1 " + i1, object.getString("id1"));
        Assert.assertEquals("id2 " + i2, object.getString("id2"));
        Assert.assertEquals("label " + i, object.getString("label"));
        Assert.assertEquals(dateTime.toString(new Timestamp(time - i * TimeUnit.Hour.getTime())), object.getString("time"));
    }
}
