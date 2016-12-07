package org.lpw.ranch.recycle;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class RecycleModelSupportTest extends TephraTestSupport {
    @Autowired
    private ModelHelper modelHelper;
    @Autowired
    private LiteOrm liteOrm;

    @Test
    public void recycle() {
        TestRecycleModel model1 = new TestRecycleModel();
        model1.setRecycle(Recycle.No.getValue());
        liteOrm.save(model1);
        TestRecycleModel model2 = liteOrm.findById(TestRecycleModel.class, model1.getId());
        Assert.assertEquals(Recycle.No.getValue(), model2.getRecycle());

        model1.setRecycle(Recycle.Yes.getValue());
        liteOrm.save(model1);
        model2 = liteOrm.findById(TestRecycleModel.class, model1.getId());
        Assert.assertEquals(Recycle.Yes.getValue(), model2.getRecycle());

        JSONObject object = modelHelper.toJson(model1);
        Assert.assertEquals(1, object.size());
        Assert.assertEquals(model1.getId(), object.getString("id"));
        Assert.assertFalse(object.has("recycle"));
    }
}
