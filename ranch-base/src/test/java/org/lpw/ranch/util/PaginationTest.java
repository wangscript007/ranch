package org.lpw.ranch.util;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class PaginationTest extends TephraTestSupport {
    @Inject
    private MockHelper mockHelper;
    @Inject
    private Pagination pagination;

    @Test
    public void getPageSize() {
        mockHelper.reset();
        mockHelper.mock("/pagination");
        Assert.assertEquals(0, pagination.getPageSize());
        Assert.assertEquals(10, pagination.getPageSize(10));
        mockHelper.getRequest().addParameter("page-size", "1");
        Assert.assertEquals(0, pagination.getPageSize());
        Assert.assertEquals(10, pagination.getPageSize(10));
        mockHelper.getRequest().addParameter("pagesize", "2");
        Assert.assertEquals(0, pagination.getPageSize());
        Assert.assertEquals(10, pagination.getPageSize(10));
        mockHelper.getRequest().addParameter("pageSize", "3");
        Assert.assertEquals(3, pagination.getPageSize());
        Assert.assertEquals(3, pagination.getPageSize(10));
    }

    @Test
    public void getPageNum() {
        mockHelper.reset();
        mockHelper.mock("/pagination");
        Assert.assertEquals(0, pagination.getPageNum());

        mockHelper.getRequest().addParameter("page-num", "1");
        Assert.assertEquals(0, pagination.getPageNum());
        mockHelper.getRequest().addParameter("pagenum", "2");
        Assert.assertEquals(0, pagination.getPageNum());
        mockHelper.getRequest().addParameter("pageNum", "3");
        Assert.assertEquals(3, pagination.getPageNum());
    }
}
