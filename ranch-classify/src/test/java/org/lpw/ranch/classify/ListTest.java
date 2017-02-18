package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author lpw
 */
public class ListTest extends TestSupport {
    @Test
    public void list() throws Exception {
        schedulerAspect.pause();
        ClassifyModel classify1111 = create(1111, false);
        create(1112, true);
        create(1211, false);
        create(1212, false);
        create(1311, false);
        create(1312, false);

        Field field = ClassifyServiceImpl.class.getDeclaredField("size");
        field.setAccessible(true);
        Object size = field.get(classifyService);
        field.set(classifyService, 3);

        mockHelper.reset();
        mockHelper.mock("/classify/list");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertEquals(3, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);
        equalsCodeKeyName(data.getJSONObject(1), 1211);
        equalsCodeKeyName(data.getJSONObject(2), 1212);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "2");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(2, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);
        equalsCodeKeyName(data.getJSONObject(1), 1211);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 12");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(2, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1211);
        equalsCodeKeyName(data.getJSONObject(1), 1212);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 11");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);

        classify1111.setKey("key 2111");
        liteOrm.save(classify1111);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 11");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);
        Assert.assertEquals(classify1111.getId(), data.getJSONObject(0).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 2");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        equalsCodeKeyName(data.getJSONObject(0), 1111);

        classifyService.refresh();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 11");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(0, data.size());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 2");
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.mock("/classify/list");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        equalsCodeKeyName(data.getJSONObject(0), "code 1111", "key 2111", "name 1111");
        Assert.assertEquals(classify1111.getId(), data.getJSONObject(0).getString("id"));

        field.set(classifyService, size);
        schedulerAspect.press();
    }
}
