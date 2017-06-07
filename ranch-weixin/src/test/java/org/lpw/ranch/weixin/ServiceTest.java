package org.lpw.ranch.weixin;


import org.junit.Assert;
import org.junit.Test;

/**
 * @author lpw
 */
public class ServiceTest extends TestSupport {
    @Test
    public void service() {
        schedulerAspect.pause();

        mockHelper.reset();
        mockHelper.mock("/weixin/wx-app-id");
        Assert.assertEquals("", mockHelper.getResponse().asString());

        schedulerAspect.press();
    }
}
