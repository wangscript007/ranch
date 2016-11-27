package org.lpw.ranch.util;

import org.lpw.tephra.ctrl.context.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @auth lpw
 */
@Controller("ranch.util.pagination")
public class PaginationImpl implements Pagination {
    @Autowired
    protected Request request;

    @Override
    public int getPageSize() {
        return request.getAsInt("pageSize");
    }

    @Override
    public int getPageNum() {
        return request.getAsInt("pageNum");
    }
}
