package org.lpw.ranch.recycle;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.TephraTestSupport;

/**
 * @author lpw
 */
public class RecycleTest extends TephraTestSupport {
    @Test
    public void recycle() {
        Assert.assertEquals(0, Recycle.No.getValue());
        Assert.assertEquals("c_recycle=0", Recycle.No.getSql());
        Assert.assertEquals(1, Recycle.Yes.getValue());
        Assert.assertEquals("c_recycle=1", Recycle.Yes.getSql());
        Assert.assertEquals(2, Recycle.values().length);
        Assert.assertEquals(Recycle.No, Recycle.values()[0]);
        Assert.assertEquals(Recycle.Yes, Recycle.values()[1]);
        Assert.assertEquals(Recycle.No, Recycle.valueOf("No"));
        Assert.assertEquals(Recycle.Yes, Recycle.valueOf("Yes"));
    }
}
