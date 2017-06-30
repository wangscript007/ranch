package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Thread;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class SaveTest extends TestSupport {
    @Inject
    private Generator generator;
    @Inject
    private Thread thread;

    @Test
    public void save() {
        mockHelper.reset();
        mockHelper.mock("/link/save");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2701, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LinkModel.NAME + ".type")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", generator.random(101));
        mockHelper.mock("/link/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2702, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(LinkModel.NAME + ".type"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.mock("/link/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2703, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LinkModel.NAME + ".id1")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.getRequest().addParameter("id1", generator.random(37));
        mockHelper.mock("/link/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2704, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(LinkModel.NAME + ".id1"), 36), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.getRequest().addParameter("id1", "id1");
        mockHelper.mock("/link/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2705, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LinkModel.NAME + ".id2")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.getRequest().addParameter("id1", "id1");
        mockHelper.getRequest().addParameter("id2", generator.random(37));
        mockHelper.mock("/link/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2706, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(LinkModel.NAME + ".id2"), 36), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.getRequest().addParameter("id1", "id1");
        mockHelper.getRequest().addParameter("id2", "id2");
        mockHelper.mock("/link/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(4, data.size());
        Assert.assertEquals("type", data.getString("type"));
        Assert.assertEquals("id1", data.getString("id1"));
        Assert.assertEquals("id2", data.getString("id2"));
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toTime(data.getString("time")).getTime() < 2000);
        Assert.assertEquals(1, liteOrm.count(new LiteQuery(LinkModel.class), null));

        thread.sleep(3, TimeUnit.Second);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.getRequest().addParameter("id1", "id1");
        mockHelper.getRequest().addParameter("id2", "id2");
        mockHelper.getRequest().addParameter("label", "label value");
        mockHelper.mock("/link/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(5, data.size());
        Assert.assertEquals("type", data.getString("type"));
        Assert.assertEquals("id1", data.getString("id1"));
        Assert.assertEquals("id2", data.getString("id2"));
        Assert.assertEquals("label value", data.getString("label"));
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toTime(data.getString("time")).getTime() < 2000);
        Assert.assertEquals(1, liteOrm.count(new LiteQuery(LinkModel.class), null));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.getRequest().addParameter("id1", "id1");
        mockHelper.getRequest().addParameter("id2", "id2");
        mockHelper.getRequest().addParameter("name", "name value");
        mockHelper.mock("/link/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(6, data.size());
        Assert.assertEquals("type", data.getString("type"));
        Assert.assertEquals("id1", data.getString("id1"));
        Assert.assertEquals("id2", data.getString("id2"));
        Assert.assertEquals("label value", data.getString("label"));
        Assert.assertEquals("name value", data.getString("name"));
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toTime(data.getString("time")).getTime() < 2000);
        Assert.assertEquals(1, liteOrm.count(new LiteQuery(LinkModel.class), null));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.getRequest().addParameter("id1", "id1 value");
        mockHelper.getRequest().addParameter("id2", "id2 value");
        mockHelper.getRequest().addParameter("label", "label value");
        mockHelper.getRequest().addParameter("name", "name value");
        mockHelper.mock("/link/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(6, data.size());
        Assert.assertEquals("type value", data.getString("type"));
        Assert.assertEquals("id1 value", data.getString("id1"));
        Assert.assertEquals("id2 value", data.getString("id2"));
        Assert.assertEquals("label value", data.getString("label"));
        Assert.assertEquals("name value", data.getString("name"));
        Assert.assertTrue(System.currentTimeMillis() - dateTime.toTime(data.getString("time")).getTime() < 2000);
        Assert.assertEquals(2, liteOrm.count(new LiteQuery(LinkModel.class), null));
    }
}
