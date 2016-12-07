package org.lpw.ranch.recycle;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class RecycleDaoTest extends TephraTestSupport {
    @Autowired
    private LiteOrm liteOrm;
    @Autowired
    private RecycleDao recycleDao;

    @Test
    public void recycle() {
        List<TestRecycleModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestRecycleModel model = new TestRecycleModel();
            model.setRecycle(i);
            liteOrm.save(model);
            list.add(model);
        }

        recycleDao.recycle(null, list.get(0).getId(), Recycle.Yes);
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestRecycleModel.class, list.get(i).getId()).getRecycle());

        recycleDao.recycle(TestRecycleModel.class, null, Recycle.Yes);
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestRecycleModel.class, list.get(i).getId()).getRecycle());

        recycleDao.recycle(TestRecycleModel.class, "", Recycle.Yes);
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestRecycleModel.class, list.get(i).getId()).getRecycle());

        recycleDao.recycle(TestRecycleModel.class, list.get(0).getId(), null);
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestRecycleModel.class, list.get(i).getId()).getRecycle());

        recycleDao.recycle(TestRecycleModel.class, list.get(0).getId(), Recycle.Yes);
        Assert.assertEquals(Recycle.Yes.getValue(), liteOrm.findById(TestRecycleModel.class, list.get(0).getId()).getRecycle());
        for (int i = 1; i < list.size(); i++)
            Assert.assertEquals(i, liteOrm.findById(TestRecycleModel.class, list.get(i).getId()).getRecycle());
    }
}
