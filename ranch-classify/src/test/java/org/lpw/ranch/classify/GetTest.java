package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.cache.Cache;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class GetTest extends TestSupport {
    @Inject
    private Cache cache;

    @Test
    public void get() {
        schedulerAspect.pause();
        cache.remove(ClassifyModel.NAME + ".service.random");
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list.add(create(i, "{\"links\":" + i + "}", i % 2 == 0));
        JSONArray links = new JSONArray();
        for (int i = 0; i < 2; i++)
            links.add(list.get(i).getId());
        JSONObject json = new JSONObject();
        json.put("links", links);
        ClassifyModel classify1 = create(11, false);
        ClassifyModel classify2 = create(22, json.toString(), false);

        mockHelper.reset();
        mockHelper.mock("/classify/get");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id," + list.get(0).getId() + "," + list.get(1).getId() + "," + list.get(1).getId());
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertFalse(data.containsKey("id"));
        Assert.assertFalse(data.containsKey(list.get(0).getId()));
        JSONObject classify = data.getJSONObject(list.get(1).getId());
        Assert.assertEquals(list.get(1).getId(), classify.getString("id"));
        equalsCodeKeyName(classify, 1);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id," + list.get(0).getId() + "," + list.get(1).getId() + "," + list.get(1).getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertFalse(data.containsKey("id"));
        Assert.assertFalse(data.containsKey(list.get(0).getId()));
        classify = data.getJSONObject(list.get(1).getId());
        Assert.assertEquals(list.get(1).getId(), classify.getString("id"));
        equalsCodeKeyName(classify, 1);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", classify1.getId());
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        classify = data.getJSONObject(classify1.getId());
        Assert.assertEquals(classify1.getId(), classify.getString("id"));
        equalsCodeKeyName(classify, 11);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", classify2.getId());
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(2, data.size());
        classify = data.getJSONObject(classify2.getId());
        Assert.assertEquals(classify2.getId(), classify.getString("id"));
        equalsCodeKeyName(classify, 22);
        classify = data.getJSONObject(list.get(1).getId());
        Assert.assertEquals(list.get(1).getId(), classify.getString("id"));
        equalsCodeKeyName(classify, 1);

        schedulerAspect.press();
    }
}
