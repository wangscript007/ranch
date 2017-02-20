package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class ListTest extends TestSupport {
    @Test
    public void list() {
        schedulerAspect.pause();
        create(1111, false);
        create(1112, true);
        create(1211, false);
        create(1212, false);
        create(1311, false);
        ClassifyModel classify1312 = create(1312, false);
        classify1312.setKey(null);
        liteOrm.save(classify1312);

        mockHelper.reset();
        mockHelper.mock("/classify/list");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1202, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".code")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertEquals(5, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);
        equalsCodeKeyName(data.getJSONObject(1), 1211);
        equalsCodeKeyName(data.getJSONObject(2), 1212);
        equalsCodeKeyName(data.getJSONObject(3), 1311);
        equalsCodeKeyName(data.getJSONObject(4), "code 1312", null, "name 1312");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(4, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);
        equalsCodeKeyName(data.getJSONObject(1), 1211);
        equalsCodeKeyName(data.getJSONObject(2), 1212);
        equalsCodeKeyName(data.getJSONObject(3), 1311);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("name", "name");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(5, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);
        equalsCodeKeyName(data.getJSONObject(1), 1211);
        equalsCodeKeyName(data.getJSONObject(2), 1212);
        equalsCodeKeyName(data.getJSONObject(3), 1311);
        equalsCodeKeyName(data.getJSONObject(4), "code 1312", null, "name 1312");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("name", "name");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(4, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);
        equalsCodeKeyName(data.getJSONObject(1), 1211);
        equalsCodeKeyName(data.getJSONObject(2), 1212);
        equalsCodeKeyName(data.getJSONObject(3), 1311);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("key", "key123");
        mockHelper.getRequest().addParameter("name", "name");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(0, data.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("name", "name123");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(0, data.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.getRequest().addParameter("key", "key123");
        mockHelper.getRequest().addParameter("name", "name123");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(0, data.size());

        classify1312.setKey("key 1312");
        liteOrm.save(classify1312);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(5, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);
        equalsCodeKeyName(data.getJSONObject(1), 1211);
        equalsCodeKeyName(data.getJSONObject(2), 1212);
        equalsCodeKeyName(data.getJSONObject(3), 1311);
        equalsCodeKeyName(data.getJSONObject(4), "code 1312", null, "name 1312");

        classifyService.refresh();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(5, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);
        equalsCodeKeyName(data.getJSONObject(1), 1211);
        equalsCodeKeyName(data.getJSONObject(2), 1212);
        equalsCodeKeyName(data.getJSONObject(3), 1311);
        equalsCodeKeyName(data.getJSONObject(4), 1312);

        schedulerAspect.press();
    }
}
