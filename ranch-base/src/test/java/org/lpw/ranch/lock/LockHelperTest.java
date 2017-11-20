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
        Assert.assertNull(lockHelper.lock(null, 10, 20));
        String id1 = lockHelper.lock("key 1", 10, 20);
        String id2 = lockHelper.lock("key 1", 10, 0);
        Assert.assertNotNull(id1);
        Assert.assertNull(id2);
        PageList<LockModel> pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals(id1, pl.getList().get(0).getId());

        String id3 = lockHelper.lock("key 1", 10, 20);
        Assert.assertNull(id3);
        String id4 = lockHelper.lock("key 4", 10, 20);
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

        String id5 = lockHelper.lock("key 1", 10, 20);
        Assert.assertNotNull(id5);
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals(id5, pl.getList().get(0).getId());
        lockHelper.unlock(id5);
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(0, pl.getList().size());

        String id6 = lockHelper.lock("key 1", 10, 3);
        Assert.assertNotNull(id6);
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals(id6, pl.getList().get(0).getId());
        long time = System.currentTimeMillis();
        Assert.assertNull(lockHelper.lock("key 1", 10 * 1000L, 1));
        Assert.assertTrue(System.currentTimeMillis() - time < 4 * 1000L);

        lockHelper.unlock(null);
    }

    @Test
    public void atomicable() {
        Assert.assertEquals(lockHelper.hashCode(), atomicable.hashCode());
        atomicable.fail(null);
        atomicable.close();

        String id = lockHelper.lock("key", 10, 20);
        Assert.assertNull(lockHelper.lock("key", 10, 20));
        PageList<LockModel> pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals(id, pl.getList().get(0).getId());
        atomicable.fail(null);
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(0, pl.getList().size());

        id = lockHelper.lock("key", 10, 20);
        Assert.assertNull(lockHelper.lock("key", 10, 20));
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals(id, pl.getList().get(0).getId());
        atomicable.close();
        pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(0, pl.getList().size());
    }

    @Test
    public void expire() throws Exception {
        Assert.assertNotNull(lockHelper.lock("key", 10 * 1000, 0));
        Assert.assertNotNull(lockHelper.lock("key 2", 10 * 1000, 20));
        Assert.assertNull(lockHelper.lock("key", 1, 0));
        Thread.sleep(6 * 1000);
        Assert.assertNotNull(lockHelper.lock("key", 10 * 1000, 0));
        Thread.sleep(6 * 1000);
        PageList<LockModel> pl = liteOrm.query(new LiteQuery(LockModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        Assert.assertEquals("key 2", pl.getList().get(0).getKey());
    }
}
