package org.lpw.ranch.recycle;

import org.lpw.tephra.dao.model.Model;

/**
 * 回收站Model。
 *
 * @author lpw
 */
public interface RecycleModel extends Model {
    /**
     * 获取回收站状态。
     *
     * @return 回收站状态。
     */
    int getRecycle();

    /**
     * 设置回收站状态。
     *
     * @param recycle 回收站状态。
     */
    void setRecycle(int recycle);
}
