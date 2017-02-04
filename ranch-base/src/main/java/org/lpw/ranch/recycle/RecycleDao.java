package org.lpw.ranch.recycle;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface RecycleDao {
    /**
     * 设置回收站状态。
     *
     * @param modelClass Model类。
     * @param id         ID值。
     * @param recycle    目标状态。
     * @param <T>        Model类。
     */
    <T extends RecycleModel> void recycle(Class<T> modelClass, String id, Recycle recycle);

    /**
     * 检索回收站中的数据集。
     *
     * @param modelClass Model类。
     * @param pageSize   每页显示记录数。
     * @param pageNum    当前显示页数。
     * @param <T>        Model类。
     * @return 回收站中的数据集。
     */
    <T extends RecycleModel> PageList<T> recycle(Class<T> modelClass, int pageSize, int pageNum);
}
