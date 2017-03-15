package org.lpw.ranch.snapshot;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.TimeUnit;

import java.sql.Timestamp;

/**
 * @author lpw
 */
public class GetTest extends TestSupport {
    private long time = System.currentTimeMillis();

    @Test
    public void get() {
        SnapshotModel snapshot1 = create(1);
        SnapshotModel snapshot2 = create(2);

        mockHelper.reset();
        mockHelper.mock("/snapshot/get");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1901, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(SnapshotModel.NAME + ".ids")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.mock("/snapshot/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/snapshot/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2," + snapshot1.getId() + ",id3," + snapshot2.getId() + ",id4," + snapshot2.getId());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/snapshot/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(2, data.size());
        equals(data.getJSONObject(snapshot1.getId()), 1);
        equals(data.getJSONObject(snapshot2.getId()), 2);
    }

    private SnapshotModel create(int i) {
        SnapshotModel snapshot = new SnapshotModel();
        snapshot.setData("data " + i);
        snapshot.setContent("content " + i);
        snapshot.setTime(new Timestamp(time - i * TimeUnit.Day.getTime()));
        liteOrm.save(snapshot);

        return snapshot;
    }

    private void equals(JSONObject object, int i) {
        Assert.assertEquals("data " + i, object.getString("data"));
        Assert.assertEquals("content " + i, object.getString("content"));
        Assert.assertEquals(dateTime.toString(new Timestamp(time - i * TimeUnit.Day.getTime())), object.getString("time"));
    }
}
