package org.lpw.ranch.util;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @auth lpw
 */
public class PaginationTest extends TephraTestSupport {
    @Autowired
    private MockHelper mockHelper;
    @Autowired
    private Pagination pagination;

    @Test
    public void getPageSize() {
        mockHelper.mock("/pagination");
        Assert.assertEquals(0, pagination.getPageSize());
        mockHelper.getRequest().addParameter("page-size", "1");
        Assert.assertEquals(0, pagination.getPageSize());
        mockHelper.getRequest().addParameter("pagesize", "2");
        Assert.assertEquals(0, pagination.getPageSize());
        mockHelper.getRequest().addParameter("pageSize", "3");
        Assert.assertEquals(3, pagination.getPageSize());
    }

    @Test
    public void getPageNum() {
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
