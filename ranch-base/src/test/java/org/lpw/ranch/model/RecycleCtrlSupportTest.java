package org.lpw.ranch.model;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class RecycleCtrlSupportTest extends TephraTestSupport {
    @Autowired
    private Message message;
    @Autowired
    private Generator generator;
    @Autowired
    private Request request;
    @Autowired
    private MockHelper mockHelper;
    @Autowired
    private TestRecycleService recycleService;

    @Test
    public void delete() {
        mockHelper.reset();
        mockHelper.mock("/model/delete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(TestModel.NAME+".id")), object.getString("message"));
        Assert.assertNull(recycleService.getDeleteId());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/model/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(TestModel.NAME+".id")), object.getString("message"));
        Assert.assertNull(recycleService.getDeleteId());

        String id = generator.uuid();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", id);
        mockHelper.mock("/model/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9991, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        Assert.assertNull(recycleService.getDeleteId());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", id);
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/model/delete");
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
        mockHelper.mock("/model/recycle");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9991, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/model/recycle");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals("recycle", data.getString("name"));
    }

    @Test
    public void restore() {
        mockHelper.reset();
        mockHelper.mock("/model/restore");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(TestModel.NAME+".id")), object.getString("message"));
        Assert.assertNull(recycleService.getRestoreId());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/model/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(TestModel.NAME+".id")), object.getString("message"));
        Assert.assertNull(recycleService.getRestoreId());

        String id = generator.uuid();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", id);
        mockHelper.mock("/model/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9991, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        Assert.assertNull(recycleService.getRestoreId());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", id);
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/model/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(id, recycleService.getRestoreId());
    }
}
