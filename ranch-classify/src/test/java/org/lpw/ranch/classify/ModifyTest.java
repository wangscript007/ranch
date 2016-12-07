package org.lpw.ranch.classify;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class ModifyTest extends TestSupport {
    @Test
    public void modify() {
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            list.add(create(i, false));

        mockHelper.reset();
        mockHelper.mock("/classify/modify");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", generator.random(101));
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1203, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".code"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", "new code");
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1205, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", "new code");
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.getRequest().addParameter("label", "new label");
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.getRequest().addParameter("code", "new code");
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.getRequest().addParameter("label", "new label");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", "new code");
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.getRequest().addParameter("label", "new label");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(list.get(0).getId(), data.getString("id"));
        equalsCodeName(data, "new code", "new name");
        Assert.assertEquals("new label", data.getString("label"));
        ClassifyModel classify = liteOrm.findById(ClassifyModel.class, list.get(0).getId());
        Assert.assertEquals("new code", classify.getCode());
        Assert.assertEquals("new name", classify.getName());
        Assert.assertEquals("new label", classify.getLabel());
        classify = liteOrm.findById(ClassifyModel.class, list.get(1).getId());
        Assert.assertEquals("code 1", classify.getCode());
        Assert.assertEquals("name 1", classify.getName());
        Assert.assertEquals("label 1", classify.getLabel());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(list.get(0).getId(), data.getString("id"));
        equalsCodeName(data, "new code", "new name");
        Assert.assertEquals("new label", data.getString("label"));
        classify = liteOrm.findById(ClassifyModel.class, list.get(0).getId());
        Assert.assertEquals("new code", classify.getCode());
        Assert.assertEquals("new name", classify.getName());
        Assert.assertEquals("new label", classify.getLabel());
        classify = liteOrm.findById(ClassifyModel.class, list.get(1).getId());
        Assert.assertEquals("code 1", classify.getCode());
        Assert.assertEquals("name 1", classify.getName());
        Assert.assertEquals("label 1", classify.getLabel());
    }
}
