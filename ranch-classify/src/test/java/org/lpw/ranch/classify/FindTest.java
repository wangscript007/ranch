package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class FindTest extends TestSupport {
    @Test
    public void find() {
        ClassifyModel classify11 = create("code 1", 1, null, false);
        create("code 1", 2, null, false);
        create("code 2", 1, null, false);
        create("code 2", 2, null, true);

        mockHelper.reset();
        mockHelper.mock("/classify/find");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1202, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".code")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1204, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 2");
        mockHelper.getRequest().addParameter("key", "key 2");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 2");
        mockHelper.getRequest().addParameter("key", "key 3");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 3");
        mockHelper.getRequest().addParameter("key", "key 1");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("key", "key 1");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(object.getJSONObject("data"), "code 1", 1);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("key", "key 2");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(object.getJSONObject("data"), "code 1", 2);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 2");
        mockHelper.getRequest().addParameter("key", "key 1");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(object.getJSONObject("data"), "code 2", 1);

        classify11.setKey("key 3");
        liteOrm.save(classify11);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("key", "key 1");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        equals(object.getJSONObject("data"), "code 1", 1);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("key", "key 3");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(36, data.getString("id").length());
        Assert.assertEquals("code 1", data.getString("code"));
        Assert.assertEquals("key 3", data.getString("key"));
        for (String name : new String[]{"code", "name"})
            Assert.assertEquals(name + " " + 1, data.get(name));

        classifyService.refresh();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("key", "key 1");
        mockHelper.mock("/classify/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());
    }

    private void equals(JSONObject object, String code, int i) {
        Assert.assertEquals(36, object.getString("id").length());
        Assert.assertEquals(code, object.getString("code"));
        for (String name : new String[]{"key", "value", "name"})
            Assert.assertEquals(name + " " + i, object.get(name));
    }
}
