package org.lpw.ranch.recycle;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class RecycleModelSupportTest extends TephraTestSupport {
    @Inject
    protected ModelHelper modelHelper;
    @Inject
    protected LiteOrm liteOrm;

    @Test
    public void recycle() {
        RecycleModel model = newModel();
        model.setRecycle(Recycle.No.getValue());
        liteOrm.save(model);
        RecycleModel model1 = liteOrm.findById(modelClass(), model.getId());
        Assert.assertEquals(Recycle.No.getValue(), model1.getRecycle());

        model.setRecycle(Recycle.Yes.getValue());
        liteOrm.save(model);
        model1 = liteOrm.findById(modelClass(), model.getId());
        Assert.assertEquals(Recycle.Yes.getValue(), model1.getRecycle());

        JSONObject object = modelHelper.toJson(model);
        Assert.assertEquals(model.getId(), object.getString("id"));
        Assert.assertFalse(object.containsKey("recycle"));
    }

    protected RecycleModel newModel() {
        return new TestRecycleModel();
    }

    protected Class<? extends RecycleModel> modelClass() {
        return TestRecycleModel.class;
    }
}
