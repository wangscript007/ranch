package org.lpw.ranch.classify;

import net.sf.json.JSONObject;
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
        Assert.assertEquals(1202, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".code")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", generator.random(101));
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1203, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".code"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1204, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".name")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1205, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("name", "name");
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("name", "name 1");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equalsCodeName(data, "code 1", "name 1");
        Assert.assertFalse(data.has("label"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 2");
        mockHelper.getRequest().addParameter("name", "name 2");
        mockHelper.getRequest().addParameter("label", "label 2");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equalsCodeName(data, "code 2", "name 2");
        Assert.assertEquals("label 2", data.getString("label"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 3");
        mockHelper.getRequest().addParameter("name", "name 3");
        mockHelper.getRequest().addParameter("label", "{\"id\":\"id\",\"name\":\"name\"}");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/classify/create");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        equalsCodeName(data, "code 3", "name 3");
        JSONObject label = data.getJSONObject("label");
        Assert.assertEquals("id", label.getString("id"));
        Assert.assertEquals("name", label.getString("name"));
    }
}
