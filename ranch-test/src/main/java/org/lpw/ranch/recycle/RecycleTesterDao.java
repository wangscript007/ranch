package org.lpw.ranch.recycle;

/**
 * @author lpw
 */
public interface RecycleTesterDao<T extends RecycleModel> {
    /**
     * 创建RecycleModel持久化数据。
     *
     * @param i       序号。
     * @param recycle 回收站状态。
     * @return Model实例。
     */
    T create(int i, Recycle recycle);

    /**
     * 检索RecycleModel持久化数据。
     *
     * @param id ID值。
     * @return Model实例。
     */
    T findById(String id);

    /**
     * 清空数据。
     */
    void clean();
}
