package org.lpw.ranch.user;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author lpw
 */
public class TypeTest extends TestSupport {
    @Test
    public void type() {
        Assert.assertEquals(UserService.Type.Bind, UserService.Type.values()[0]);
        Assert.assertEquals(UserService.Type.Self, UserService.Type.values()[1]);
        Assert.assertEquals(UserService.Type.WeiXin, UserService.Type.values()[2]);
        Assert.assertEquals(UserService.Type.Bind, UserService.Type.valueOf("Bind"));
        Assert.assertEquals(UserService.Type.Self, UserService.Type.valueOf("Self"));
        Assert.assertEquals(UserService.Type.WeiXin, UserService.Type.valueOf("WeiXin"));
    }
}
