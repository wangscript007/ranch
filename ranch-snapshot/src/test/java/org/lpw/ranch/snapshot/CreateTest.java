package org.lpw.ranch.snapshot;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.TimeUnit;

/**
 * @author lpw
 */
public class CreateTest extends TestSupport {
    @Test
    public void create() {
        mockHelper.reset();
        mockHelper.mock("/snapshot/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1902, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(SnapshotModel.NAME + ".data")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("data", "data value");
        mockHelper.mock("/snapshot/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1903, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(SnapshotModel.NAME + ".content")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("data", "data value");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.mock("/snapshot/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("data", "data value");
        mockHelper.getRequest().addParameter("content", "content value");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/snapshot/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        String id = object.getString("data");
        SnapshotModel snapshot = liteOrm.findById(SnapshotModel.class, id);
        Assert.assertEquals("data value", snapshot.getData());
        Assert.assertEquals("content value", snapshot.getContent());
        Assert.assertTrue(System.currentTimeMillis() - snapshot.getTime().getTime() < 2000L);
        Assert.assertEquals(digest.md5("data valuecontent value"), snapshot.getMd5());

        thread.sleep(3, TimeUnit.Second);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("data", "data value");
        mockHelper.getRequest().addParameter("content", "content value");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/snapshot/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals(id, object.getString("data"));
        snapshot = liteOrm.findById(SnapshotModel.class, id);
        Assert.assertEquals("data value", snapshot.getData());
        Assert.assertEquals("content value", snapshot.getContent());
        Assert.assertTrue(System.currentTimeMillis() - snapshot.getTime().getTime() > 2000L);
        Assert.assertTrue(System.currentTimeMillis() - snapshot.getTime().getTime() < 5000L);
        Assert.assertEquals(digest.md5("data valuecontent value"), snapshot.getMd5());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("data", "data value ");
        mockHelper.getRequest().addParameter("content", "content value");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/snapshot/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertNotEquals(id, object.getString("data"));
        snapshot = liteOrm.findById(SnapshotModel.class, object.getString("data"));
        Assert.assertEquals("data value ", snapshot.getData());
        Assert.assertEquals("content value", snapshot.getContent());
        Assert.assertTrue(System.currentTimeMillis() - snapshot.getTime().getTime() < 2000L);
        Assert.assertEquals(digest.md5("data value content value"), snapshot.getMd5());
    }
}
