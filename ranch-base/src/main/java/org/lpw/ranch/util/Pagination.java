package org.lpw.ranch.util;

/**
 * 分页。
 *
 * @auth lpw
 */
public interface Pagination {
    /**
     * 获取每页显示记录数。
     *
     * @return 每页显示记录数。
     */
    int getPageSize();

    /**
     * 获取当前显示页数。
     *
     * @return 当前显示页数。
     */
    int getPageNum();
}
