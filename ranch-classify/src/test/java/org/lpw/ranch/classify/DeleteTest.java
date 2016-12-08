package org.lpw.ranch.classify;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class DeleteTest extends TestSupport {
    @Test
    public void delete() {
        List<ClassifyModel> list = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            list.add(create(i, false));

        mockHelper.reset();
        mockHelper.mock("/classify/delete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/classify/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1201, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(ClassifyModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/classify/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        list.forEach(classify -> Assert.assertEquals(Recycle.No.getValue(), liteOrm.findById(ClassifyModel.class, classify.getId()).getRecycle()));

        String cacheKey = ClassifyModel.NAME + ".service.random";
        cache.remove(cacheKey);
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(1).getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/delete");
        String random = cache.get(cacheKey);
        Assert.assertEquals(32, random.length());
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0, size = list.size(); i < size; i++) {
            ClassifyModel classify = liteOrm.findById(ClassifyModel.class, list.get(i).getId());
            if (i == 1 || i > 9) {
                Assert.assertEquals(Recycle.Yes.getValue(), classify.getRecycle());
                Assert.assertFalse(classifyService.getJsons(new String[]{classify.getId()}, false).has(classify.getId()));
            } else {
                Assert.assertEquals(Recycle.No.getValue(), classify.getRecycle());
                Assert.assertTrue(classifyService.getJsons(new String[]{classify.getId()}, false).has(classify.getId()));
            }
        }
    }
}
