package org.lpw.ranch.recycle;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class RecycleCtrlSupportTest extends TephraTestSupport {
    @Inject
    private Message message;
    @Inject
    private Generator generator;
    @Inject
    private Sign sign;
    @Inject
    private MockHelper mockHelper;
    @Inject
    private TestRecycleService recycleService;

    @Test
    public void delete() {
        mockHelper.reset();
        mockHelper.mock("/recycle/delete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(TestRecycleModel.NAME + ".id")), object.getString("message"));
        Assert.assertNull(recycleService.getDeleteId());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/recycle/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(TestRecycleModel.NAME + ".id")), object.getString("message"));
        Assert.assertNull(recycleService.getDeleteId());

        String id = generator.uuid();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", id);
        mockHelper.mock("/recycle/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9991, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        Assert.assertNull(recycleService.getDeleteId());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", id);
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/recycle/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(id, recycleService.getDeleteId());
    }

    @Test
    public void recycle() {
        JSONObject json = new JSONObject();
        json.put("name", "recycle");
        recycleService.setRecycle(json);

        mockHelper.reset();
        mockHelper.mock("/recycle/recycle");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9991, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/recycle/recycle");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals("recycle", data.getString("name"));
    }

    @Test
    public void restore() {
        mockHelper.reset();
        mockHelper.mock("/recycle/restore");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(TestRecycleModel.NAME + ".id")), object.getString("message"));
        Assert.assertNull(recycleService.getRestoreId());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/recycle/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(TestRecycleModel.NAME + ".id")), object.getString("message"));
        Assert.assertNull(recycleService.getRestoreId());

        String id = generator.uuid();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", id);
        mockHelper.mock("/recycle/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9991, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        Assert.assertNull(recycleService.getRestoreId());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", id);
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/recycle/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(id, recycleService.getRestoreId());
    }
}
