package org.lpw.ranch.classify;

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
        mockHelper.mock("/classify/create");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1202, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".code")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", generator.random(101));
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1203, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".code"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1204, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".name")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1205, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("name", "name");
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("name", "name 1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equalsCodeName(data, "code 1", "name 1");
        Assert.assertFalse(data.containsKey("label"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 2");
        mockHelper.getRequest().addParameter("name", "name 2");
        mockHelper.getRequest().addParameter("label", "label 2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equalsCodeName(data, "code 2", "name 2");
        Assert.assertEquals("label 2", data.getString("label"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 3");
        mockHelper.getRequest().addParameter("name", "name 3");
        mockHelper.getRequest().addParameter("label", "{\"id\":\"id\",\"name\":\"name\"}");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equalsCodeName(data, "code 3", "name 3");
        JSONObject label = data.getJSONObject("label");
        Assert.assertEquals("id", label.getString("id"));
        Assert.assertEquals("name", label.getString("name"));
    }
}
