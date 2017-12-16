package org.lpw.ranch.logger;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Thread;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class LoggerServiceTest extends TephraTestSupport {
    @Inject
    private Generator generator;
    @Inject
    private Thread thread;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private LoggerService loggerService;

    @Test
    public void create() {
        Assert.assertEquals(0, liteOrm.count(new LiteQuery(LoggerModel.class), null));

        loggerService.create("logger-service");
        PageList<LoggerModel> pl = liteOrm.query(new LiteQuery(LoggerModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        LoggerModel logger = pl.getList().get(0);
        Assert.assertEquals("logger-service", logger.getKey());
        Assert.assertNull(logger.getP0());
        Assert.assertNull(logger.getP1());
        Assert.assertNull(logger.getP2());
        Assert.assertNull(logger.getP3());
        Assert.assertNull(logger.getP4());
        Assert.assertNull(logger.getP5());
        Assert.assertNull(logger.getP6());
        Assert.assertNull(logger.getP7());
        Assert.assertNull(logger.getP8());
        Assert.assertNull(logger.getP9());
        Assert.assertTrue(System.currentTimeMillis() - logger.getTime().getTime() < 1000L);

        thread.sleep(2, TimeUnit.Second);
        String string = generator.random(128);
        loggerService.create("logger-service2", "p0", string, "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10", "p11", "p12");
        pl = liteOrm.query(new LiteQuery(LoggerModel.class).order("c_time"), null);
        Assert.assertEquals(2, pl.getList().size());
        logger = pl.getList().get(0);
        Assert.assertEquals("logger-service", logger.getKey());
        Assert.assertNull(logger.getP0());
        Assert.assertNull(logger.getP1());
        Assert.assertNull(logger.getP2());
        Assert.assertNull(logger.getP3());
        Assert.assertNull(logger.getP4());
        Assert.assertNull(logger.getP5());
        Assert.assertNull(logger.getP6());
        Assert.assertNull(logger.getP7());
        Assert.assertNull(logger.getP8());
        Assert.assertNull(logger.getP9());
        Assert.assertTrue(System.currentTimeMillis() - logger.getTime().getTime() > 1000L);
        Assert.assertTrue(System.currentTimeMillis() - logger.getTime().getTime() < 3000L);
        logger = pl.getList().get(1);
        Assert.assertEquals("logger-service2", logger.getKey());
        Assert.assertEquals("p0", logger.getP0());
        Assert.assertEquals(string.substring(0, 100), logger.getP1());
        Assert.assertEquals("p2", logger.getP2());
        Assert.assertEquals("p3", logger.getP3());
        Assert.assertEquals("p4", logger.getP4());
        Assert.assertEquals("p5", logger.getP5());
        Assert.assertEquals("p6", logger.getP6());
        Assert.assertEquals("p7", logger.getP7());
        Assert.assertEquals("p8", logger.getP8());
        Assert.assertEquals("p9", logger.getP9());
        Assert.assertTrue(System.currentTimeMillis() - logger.getTime().getTime() < 1000L);
    }
}
