package org.lpw.ranch.classify;

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
    public void recycle() {
        recycleTester.recycle(this, ClassifyModel.NAME, "classify");
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
    public void clean() {
        liteOrm.delete(new LiteQuery(ClassifyModel.class), null);
    }
}
