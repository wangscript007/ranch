package org.lpw.ranch.lock;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.atomic.Atomicable;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.TephraTestSupport;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * @author lpw
 */
public class LockHelperTest extends TephraTestSupport {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private LockHelper lockHelper;
    @Resource(name = LockModel.NAME + ".helper")
    private Atomicable atomicable;

    @Test
    public void lockUnlock() {
        Assert.assertNull(lockHelper.lock(null, 10));
        String id1 = lockHelper.lock("key 1", 10);
        String id2 = lockHelper.lock("key 1", 10);
        Assert.assertNotNull(id1);
        Assert.assertNull(id2);
        PageList<LockModel> pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals(id1, pl.getList().get(0).getId());

        String id3 = lockHelper.lock("key 1", 10);
        Assert.assertNull(id3);
        String id4 = lockHelper.lock("key 4", 10);
        Assert.assertNotNull(id4);
        pl = liteOrm.query(new LiteQuery(LockModel.class).order("c_index"), null);
        Assert.assertEquals(2, pl.getList().size());
        Assert.assertEquals(id1, pl.getList().get(0).getId());
        Assert.assertEquals(id4, pl.getList().get(1).getId());

        lockHelper.unlock(id4);
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals(id1, pl.getList().get(0).getId());

        lockHelper.unlock(id1);
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(0, pl.getList().size());

        String id5 = lockHelper.lock("key 1", 10);
        Assert.assertNotNull(id5);
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals(id5, pl.getList().get(0).getId());
        lockHelper.unlock(id5);
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(0, pl.getList().size());
    }

    @Test
    public void atomicable() {
        Assert.assertEquals(lockHelper.hashCode(), atomicable.hashCode());
        atomicable.fail(null);
        atomicable.close();

        String id = lockHelper.lock("key", 10);
        Assert.assertNull(lockHelper.lock("key", 10));
        PageList<LockModel> pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals(id, pl.getList().get(0).getId());
        atomicable.fail(null);
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(0, pl.getList().size());


        id = lockHelper.lock("key", 10);
        Assert.assertNull(lockHelper.lock("key", 10));
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals(id, pl.getList().get(0).getId());
        atomicable.close();
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(0, pl.getList().size());
    }
}
