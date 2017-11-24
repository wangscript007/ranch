package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Test
    public void query() {
        for (int i = 0; i < 20; i++) {
            ClassifyModel classify = new ClassifyModel();
            classify.setCode("code " + numeric.toString(i, "00"));
            classify.setKey("key " + numeric.toString(i, "00"));
            classify.setValue("value " + numeric.toString(i, "00"));
            classify.setName("name " + numeric.toString(i, "00"));
            classify.setRecycle((i % 2 == 0 ? Recycle.No : Recycle.Yes).getValue());
            liteOrm.save(classify);
        }

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/classify/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(10, 20, 1, data);
        JSONArray array = data.getJSONArray("list");
        for (int i = 0, size = array.size(); i < size; i++)
            equals(array.getJSONObject(i), "code " + numeric.toString(2 * i, "00"),
                    "key " + numeric.toString(2 * i, "00"),
                    "value " + numeric.toString(2 * i, "00"),
                    "name " + numeric.toString(2 * i, "00"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 1");
        mockHelper.getRequest().addParameter("name", "name 1");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(10, 20, 1, data);
        array = data.getJSONArray("list");
        for (int i = 0, size = array.size(); i < size; i++)
            equals(array.getJSONObject(i), "code " + numeric.toString(2 * i, "00"),
                    "key " + numeric.toString(2 * i, "00"),
                    "value " + numeric.toString(2 * i, "00"),
                    "name " + numeric.toString(2 * i, "00"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(5, 20, 1, data);
        array = data.getJSONArray("list");
        for (int i = 0; i < 5; i++)
            equals(array.getJSONObject(i), "code " + (10 + 2 * i), "key " + (10 + 2 * i),
                    "value " + (10 + 2 * i), "name " + (10 + 2 * i));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("key", "key 1");
        mockHelper.getRequest().addParameter("name", "name 1");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(5, 10, 1, data);
        array = data.getJSONArray("list");
        for (int i = 0; i < 5; i++)
            equals(array.getJSONObject(i), "code " + (10 + 2 * i), "key " + (10 + 2 * i),
                    "value " + (10 + 2 * i), "name " + (10 + 2 * i));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.getRequest().addParameter("value", "value 2");
        mockHelper.getRequest().addParameter("name", "name 3");
        mockHelper.getRequest().addParameter("pageSize", "10");
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 10, 1, data);
        Assert.assertTrue(data.getJSONArray("list").isEmpty());

        String code = generator.random(32);
        for (int i = 0; i < 10; i++)
            create(code, i, null, false);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", code);
        mockHelper.getRequest().addParameter("pageNum", "1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(10, 20, 1, data);
        JSONArray list = data.getJSONArray("list");
        Assert.assertEquals(10, list.size());
        for (int i = 0; i < 10; i++)
            equals(list.getJSONObject(i), code, "key " + i, "value " + i, "name " + i);
    }
}
