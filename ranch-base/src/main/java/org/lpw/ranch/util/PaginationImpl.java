package org.lpw.ranch.util;

import org.lpw.tephra.ctrl.context.Request;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller("ranch.util.pagination")
public class PaginationImpl implements Pagination {
    @Inject
    private Request request;

    @Override
    public int getPageSize() {
        return request.getAsInt("pageSize");
    }

    @Override
    public int getPageSize(int defaultSize) {
        int pageSize = getPageSize();

        return pageSize <= 0 ? defaultSize : pageSize;
    }

    @Override
    public int getPageNum() {
        return request.getAsInt("pageNum");
    }
}
