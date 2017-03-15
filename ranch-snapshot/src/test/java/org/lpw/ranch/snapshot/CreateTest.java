package org.lpw.ranch.snapshot;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

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
        SnapshotModel snapshot = liteOrm.findById(SnapshotModel.class, object.getString("data"));
        Assert.assertEquals("data value", snapshot.getData());
        Assert.assertEquals("content value", snapshot.getContent());
        Assert.assertTrue(System.currentTimeMillis() - snapshot.getTime().getTime() < 2000L);
    }
}
