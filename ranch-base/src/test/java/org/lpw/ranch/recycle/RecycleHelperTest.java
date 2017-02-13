package org.lpw.ranch.recycle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lpw
 */
public class RecycleHelperTest extends TephraTestSupport {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private RecycleHelper recycleHelper;
    @Inject
    private MockHelper mockHelper;

    @Test
    public void delete() {
        List<TestRecycleModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestRecycleModel model = new TestRecycleModel();
            model.setRecycle(i);
            liteOrm.save(model);
            list.add(model);
        }

        recycleHelper.delete(null, list.get(0).getId());
        equals(list);

        recycleHelper.delete(TestRecycleModel.class, null);
        equals(list);

        recycleHelper.delete(TestRecycleModel.class, "");
        equals(list);

        recycleHelper.delete(TestRecycleModel.class, list.get(0).getId());
        Assert.assertEquals(Recycle.Yes.getValue(), liteOrm.findById(TestRecycleModel.class, list.get(0).getId()).getRecycle());
        for (int i = 1; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestRecycleModel.class, list.get(i).getId()).getRecycle());
    }

    @Test
    public void restore() {
        List<TestRecycleModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestRecycleModel model = new TestRecycleModel();
            model.setRecycle(i);
            liteOrm.save(model);
            list.add(model);
        }

        recycleHelper.restore(null, list.get(0).getId());
        equals(list);

        recycleHelper.restore(TestRecycleModel.class, null);
        equals(list);

        recycleHelper.restore(TestRecycleModel.class, "");
        equals(list);

        recycleHelper.restore(TestRecycleModel.class, list.get(list.size() - 1).getId());
        Assert.assertEquals(Recycle.No.getValue(), liteOrm.findById(TestRecycleModel.class, list.get(list.size() - 1).getId()).getRecycle());
        for (int i = 0; i < list.size() - 1; i++)
            Assert.assertEquals(i, liteOrm.findById(TestRecycleModel.class, list.get(i).getId()).getRecycle());
    }

    private void equals(List<TestRecycleModel> list) {
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestRecycleModel.class, list.get(i).getId()).getRecycle());
    }

    @Test
    public void recycle() {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestRecycleModel model = new TestRecycleModel();
            model.setRecycle(i % 2);
            liteOrm.save(model);
            if (i % 2 == 1)
                ids.add(model.getId());
        }
        Collections.sort(ids);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "20");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/recycle/recycle");
        JSONObject object = recycleHelper.recycle(TestRecycleModel.class);
        Assert.assertEquals(5, object.getIntValue("count"));
        Assert.assertEquals(20, object.getIntValue("size"));
        Assert.assertEquals(1, object.getIntValue("number"));
        JSONArray list = object.getJSONArray("list");
        Assert.assertEquals(5, list.size());
        for (int i = 0; i < 5; i++)
            Assert.assertEquals(ids.get(i), list.getJSONObject(i).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("pageSize", "2");
        mockHelper.getRequest().addParameter("pageNum", "1");
        mockHelper.mock("/recycle/recycle");
        object = recycleHelper.recycle(TestRecycleModel.class);
        Assert.assertEquals(5, object.getIntValue("count"));
        Assert.assertEquals(2, object.getIntValue("size"));
        Assert.assertEquals(1, object.getIntValue("number"));
        list = object.getJSONArray("list");
        Assert.assertEquals(2, list.size());
        for (int i = 0; i < 2; i++)
            Assert.assertEquals(ids.get(i), list.getJSONObject(i).getString("id"));
    }
}
