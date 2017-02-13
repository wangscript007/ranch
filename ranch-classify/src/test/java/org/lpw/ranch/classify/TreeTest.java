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
public class TreeTest extends TestSupport {
    @Test
    public void tree() {
        schedulerAspect.pause();
        cache.remove(ClassifyModel.NAME + ".service.tree:");
        ClassifyModel classify1a = create(1, false);
        ClassifyModel classify1b = create(1, false);
        create(1, true);
        ClassifyModel classify11 = create(11, false);
        ClassifyModel classify12 = create(12, false);
        ClassifyModel classify111 = create(111, false);
        ClassifyModel classify121 = create(121, false);
        ClassifyModel classify2 = create(2345, false);

        mockHelper.reset();
        mockHelper.mock("/classify/tree");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1202, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(ClassifyModel.NAME + ".code")), object.getString("message"));

        for (int i = 0; i < 5; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("code", "code");
            mockHelper.mock("/classify/tree");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            JSONArray data = object.getJSONArray("data");
            Assert.assertEquals(3, data.size());
            if (classify1a.getId().compareTo(classify1b.getId()) < 0) {
                Assert.assertEquals(classify1a.getId(), data.getJSONObject(0).getString("id"));
                Assert.assertEquals(classify1b.getId(), data.getJSONObject(1).getString("id"));
            } else {
                Assert.assertEquals(classify1b.getId(), data.getJSONObject(0).getString("id"));
                Assert.assertEquals(classify1a.getId(), data.getJSONObject(1).getString("id"));
            }
            Assert.assertEquals(classify2.getId(), data.getJSONObject(2).getString("id"));
            JSONArray array = data.getJSONObject(0).getJSONArray("children");
            Assert.assertEquals(2, array.size());
            Assert.assertEquals(classify11.getId(), array.getJSONObject(0).getString("id"));
            Assert.assertEquals(classify12.getId(), array.getJSONObject(1).getString("id"));
            Assert.assertFalse(data.getJSONObject(1).containsKey("children"));
            JSONArray children = array.getJSONObject(0).getJSONArray("children");
            Assert.assertEquals(1, children.size());
            Assert.assertEquals(classify111.getId(), children.getJSONObject(0).getString("id"));
            children = array.getJSONObject(1).getJSONArray("children");
            Assert.assertEquals(1, children.size());
            Assert.assertEquals(classify121.getId(), children.getJSONObject(0).getString("id"));

            classify1a.setRecycle(Recycle.Yes.getValue());
            liteOrm.save(classify1a);
            classify121.setRecycle(Recycle.Yes.getValue());
            liteOrm.save(classify121);
        }

        classifyService.refresh();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/classify/tree");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertEquals(2, data.size());
        Assert.assertEquals(classify1b.getId(), data.getJSONObject(0).getString("id"));
        Assert.assertEquals(classify2.getId(), data.getJSONObject(1).getString("id"));
        JSONArray array = data.getJSONObject(0).getJSONArray("children");
        Assert.assertEquals(2, array.size());
        Assert.assertEquals(classify11.getId(), array.getJSONObject(0).getString("id"));
        Assert.assertEquals(classify12.getId(), array.getJSONObject(1).getString("id"));
        JSONArray children = array.getJSONObject(0).getJSONArray("children");
        Assert.assertEquals(1, children.size());
        Assert.assertEquals(classify111.getId(), children.getJSONObject(0).getString("id"));
        Assert.assertFalse(array.getJSONObject(1).containsKey("children"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 1");
        mockHelper.mock("/classify/tree");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        Assert.assertEquals(classify1b.getId(), data.getJSONObject(0).getString("id"));
        array = data.getJSONObject(0).getJSONArray("children");
        Assert.assertEquals(2, array.size());
        Assert.assertEquals(classify11.getId(), array.getJSONObject(0).getString("id"));
        Assert.assertEquals(classify12.getId(), array.getJSONObject(1).getString("id"));
        children = array.getJSONObject(0).getJSONArray("children");
        Assert.assertEquals(1, children.size());
        Assert.assertEquals(classify111.getId(), children.getJSONObject(0).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("code", "code 3");
        mockHelper.mock("/classify/tree");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertTrue(data.isEmpty());
        schedulerAspect.press();
    }
}
