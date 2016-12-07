package org.lpw.ranch.classify;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class GetTest extends TestSupport {
    @Test
    public void get() {
        mockScheduler.pause();
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list.add(create(i, null, i % 2 == 0));
        ClassifyModel classify1 = create(11, false);
        JSONObject label = new JSONObject();
        for (int i = 0; i < 2; i++)
            label.put(list.get(i).getId(), list.get(i).getName());
        ClassifyModel classify2 = create(22, label.toString(), false);
        label.put("links", false);
        ClassifyModel classify3 = create(33, label.toString(), false);
        label.put("links", true);
        ClassifyModel classify4 = create(44, label.toString(), false);

        mockHelper.reset();
        mockHelper.mock("/classify/get");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id," + list.get(0).getId() + "," + list.get(1).getId() + "," + list.get(1).getId());
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertFalse(data.has("id"));
        Assert.assertFalse(data.has(list.get(0).getId()));
        JSONObject classify = data.getJSONObject(list.get(1).getId());
        Assert.assertEquals(list.get(1).getId(), classify.getString("id"));
        equalsCodeName(classify, "code 1", "name 1");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id," + list.get(0).getId() + "," + list.get(1).getId() + "," + list.get(1).getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertFalse(data.has("id"));
        Assert.assertFalse(data.has(list.get(0).getId()));
        classify = data.getJSONObject(list.get(1).getId());
        Assert.assertEquals(list.get(1).getId(), classify.getString("id"));
        equalsCodeName(classify, "code 1", "name 1");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", classify1.getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        classify = data.getJSONObject(classify1.getId());
        Assert.assertEquals(classify1.getId(), classify.getString("id"));
        equalsCodeName(classify, "code 11", "name 11");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", classify2.getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        classify = data.getJSONObject(classify2.getId());
        Assert.assertEquals(classify2.getId(), classify.getString("id"));
        equalsCodeName(classify, "code 22", "name 22");

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", classify3.getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        classify = data.getJSONObject(classify3.getId());
        Assert.assertEquals(classify3.getId(), classify.getString("id"));
        equalsCodeName(classify, "code 33", "name 33");

        for (int i = 0; i < 2; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("ids", classify4.getId());
            mockHelper.getRequest().addParameter("links", "true");
            mockHelper.mock("/classify/get");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getInt("code"));
            data = object.getJSONObject("data");
            Assert.assertEquals(2, data.size());
            classify = data.getJSONObject(classify4.getId());
            Assert.assertEquals(classify4.getId(), classify.getString("id"));
            equalsCodeName(classify, "code 44", "name 44");
            classify = data.getJSONObject(list.get(1).getId());
            Assert.assertEquals(list.get(1).getId(), classify.getString("id"));
            equalsCodeName(classify, "code 1", "name 1");

            classify4.setLabel("new label 4");
            liteOrm.save(classify4);
        }

        cache.remove(ClassifyModel.NAME + ".service.random");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", classify4.getId());
        mockHelper.getRequest().addParameter("links", "true");
        mockHelper.mock("/classify/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        classify = data.getJSONObject(classify4.getId());
        Assert.assertEquals(classify4.getId(), classify.getString("id"));
        equalsCodeName(classify, "code 44", "name 44");
        Assert.assertEquals("new label 4", classify.getString("label"));

        mockScheduler.press();
    }
}
