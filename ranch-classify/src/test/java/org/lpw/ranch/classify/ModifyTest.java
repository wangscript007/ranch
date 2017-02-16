package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        list.add(create(2, null, false));

        mockHelper.reset();
        mockHelper.mock("/classify/modify");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", generator.random(101));
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1203, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".code"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1204, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1206, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(ClassifyModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", "new code");
        mockHelper.getRequest().addParameter("key", "new key");
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.getRequest().addParameter("label", "new label");
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.getRequest().addParameter("code", "new code");
        mockHelper.getRequest().addParameter("key", "new key");
        mockHelper.getRequest().addParameter("name", "new name");
        mockHelper.getRequest().addParameter("label", "new label");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1207, object.getIntValue("code"));
        Assert.assertEquals(message.get(ClassifyModel.NAME + ".not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("code", "code 11");
        mockHelper.getRequest().addParameter("key", "key 11");
        mockHelper.getRequest().addParameter("name", "name 11");
        mockHelper.getRequest().addParameter("json", "json 11");
        mockHelper.getRequest().addParameter("label", "label 11");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(list.get(0).getId(), data.getString("id"));
        equalsCodeKeyName(data, 11);
        Assert.assertEquals("json 11", data.getString("json"));
        Assert.assertEquals("label 11", data.getString("label"));
        ClassifyModel classify = liteOrm.findById(ClassifyModel.class, list.get(0).getId());
        Assert.assertEquals("code 11", classify.getCode());
        Assert.assertEquals("key 11", classify.getKey());
        Assert.assertEquals("name 11", classify.getName());
        JSONObject json = JSON.parseObject(classify.getJson());
        Assert.assertEquals("json 11", json.getString("json"));
        Assert.assertEquals("label 11", json.getString("label"));
        classify = liteOrm.findById(ClassifyModel.class, list.get(1).getId());
        Assert.assertEquals("code 1", classify.getCode());
        Assert.assertEquals("key 1", classify.getKey());
        Assert.assertEquals("name 1", classify.getName());
        Assert.assertEquals("{\"json\":1}", classify.getJson());
        classify = liteOrm.findById(ClassifyModel.class, list.get(2).getId());
        Assert.assertEquals("code 2", classify.getCode());
        Assert.assertEquals("key 2", classify.getKey());
        Assert.assertEquals("name 2", classify.getName());
        Assert.assertNull(classify.getJson());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(list.get(0).getId(), data.getString("id"));
        equalsCodeKeyName(data, 11);
        Assert.assertEquals("json 11", data.getString("json"));
        Assert.assertEquals("label 11", data.getString("label"));
        classify = liteOrm.findById(ClassifyModel.class, list.get(0).getId());
        Assert.assertEquals("code 11", classify.getCode());
        Assert.assertEquals("key 11", classify.getKey());
        Assert.assertEquals("name 11", classify.getName());
        json = JSON.parseObject(classify.getJson());
        Assert.assertEquals("json 11", json.getString("json"));
        Assert.assertEquals("label 11", json.getString("label"));
        classify = liteOrm.findById(ClassifyModel.class, list.get(1).getId());
        Assert.assertEquals("code 1", classify.getCode());
        Assert.assertEquals("key 1", classify.getKey());
        Assert.assertEquals("name 1", classify.getName());
        Assert.assertEquals("{\"json\":1}", classify.getJson());
        classify = liteOrm.findById(ClassifyModel.class, list.get(2).getId());
        Assert.assertEquals("code 2", classify.getCode());
        Assert.assertEquals("key 2", classify.getKey());
        Assert.assertEquals("name 2", classify.getName());
        Assert.assertNull(classify.getJson());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(2).getId());
        mockHelper.getRequest().addParameter("code", "code 22");
        mockHelper.getRequest().addParameter("key", "key 22");
        mockHelper.getRequest().addParameter("name", "name 22");
        mockHelper.getRequest().addParameter("label", "label 22");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/modify");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(list.get(2).getId(), data.getString("id"));
        equalsCodeKeyName(data, 22);
        Assert.assertEquals("label 22", data.getString("label"));
        classify = liteOrm.findById(ClassifyModel.class, list.get(2).getId());
        Assert.assertEquals("code 22", classify.getCode());
        Assert.assertEquals("key 22", classify.getKey());
        Assert.assertEquals("name 22", classify.getName());
        Assert.assertEquals("{\"label\":\"label 22\"}", classify.getJson());
        classify = liteOrm.findById(ClassifyModel.class, list.get(0).getId());
        Assert.assertEquals("code 11", classify.getCode());
        Assert.assertEquals("key 11", classify.getKey());
        Assert.assertEquals("name 11", classify.getName());
        json = JSON.parseObject(classify.getJson());
        Assert.assertEquals("json 11", json.getString("json"));
        Assert.assertEquals("label 11", json.getString("label"));
        classify = liteOrm.findById(ClassifyModel.class, list.get(1).getId());
        Assert.assertEquals("code 1", classify.getCode());
        Assert.assertEquals("key 1", classify.getKey());
        Assert.assertEquals("name 1", classify.getName());
        Assert.assertEquals("{\"json\":1}", classify.getJson());
    }
}
