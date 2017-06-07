package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.ranch.recycle.RecycleModel;
import org.lpw.ranch.recycle.RecycleTester;
import org.lpw.ranch.recycle.RecycleTesterDao;
import org.lpw.tephra.dao.orm.lite.LiteQuery;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class RecycleTest extends TestSupport implements RecycleTesterDao<RecycleModel> {
    @Inject
    private RecycleTester recycleTester;

    @Test
    public void all() {
        recycleTester.all(this, ClassifyModel.NAME, "classify", 12);
    }

    @Test
    public void restore() {
        ClassifyModel classify1 = create(1, false);
        ClassifyModel classify2 = create(1, true);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/restore");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1209, object.getIntValue("code"));
        Assert.assertEquals(message.get(ClassifyModel.NAME + ".not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", classify2.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1210, object.getIntValue("code"));
        Assert.assertEquals(message.get(ClassifyModel.NAME + ".code-key.exists"), object.getString("message"));

        liteOrm.delete(classify1);
        classifyService.refresh();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", classify2.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/restore");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals(Recycle.No.getValue(), liteOrm.findById(ClassifyModel.class, classify2.getId()).getRecycle());
    }

    @Override
    public RecycleModel create(int i, Recycle recycle) {
        return create(i, recycle == Recycle.Yes);
    }

    @Override
    public RecycleModel findById(String id) {
        return liteOrm.findById(ClassifyModel.class, id);
    }

    @Override
    public void clear() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
    }
}
