package org.lpw.ranch.account.log;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author lpw
 */
public class LogServiceTest extends TestSupport {
    @Test
    public void state() {
        Assert.assertEquals(LogService.State.New, LogService.State.valueOf("New"));
        Assert.assertEquals(LogService.State.Pass, LogService.State.valueOf("Pass"));
        Assert.assertEquals(LogService.State.Reject, LogService.State.valueOf("Reject"));
        Assert.assertEquals(LogService.State.Complete, LogService.State.valueOf("Complete"));
        Assert.assertEquals(4, LogService.State.values().length);
        Assert.assertEquals(LogService.State.New, LogService.State.values()[0]);
        Assert.assertEquals(LogService.State.Pass, LogService.State.values()[1]);
        Assert.assertEquals(LogService.State.Reject, LogService.State.values()[2]);
        Assert.assertEquals(LogService.State.Complete, LogService.State.values()[3]);
    }
}
