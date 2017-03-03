package org.lpw.ranch.group.member;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author lpw
 */
public class TypeTest extends TestSupport {
    @Test
    public void type() {
        Assert.assertEquals(4, MemberService.Type.values().length);
        Assert.assertEquals(MemberService.Type.New, MemberService.Type.values()[0]);
        Assert.assertEquals(MemberService.Type.Normal, MemberService.Type.values()[1]);
        Assert.assertEquals(MemberService.Type.Manager, MemberService.Type.values()[2]);
        Assert.assertEquals(MemberService.Type.Owner, MemberService.Type.values()[3]);
        Assert.assertEquals(MemberService.Type.New, MemberService.Type.valueOf("New"));
        Assert.assertEquals(MemberService.Type.Normal, MemberService.Type.valueOf("Normal"));
        Assert.assertEquals(MemberService.Type.Manager, MemberService.Type.valueOf("Manager"));
        Assert.assertEquals(MemberService.Type.Owner, MemberService.Type.valueOf("Owner"));
    }
}
