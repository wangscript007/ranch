package org.lpw.ranch.recycle;

/**
 * @author lpw
 */
public interface RecycleDao {
    /**
     * 设置回收站状态。
     *
     * @param modelClass Model类。
     * @param id         ID值。
     * @param recycle    目标状态。
     * @param <T>        Model类。
     */
    <T extends RecycleModel> void recycle(Class<T> modelClass, String id, Recycle recycle);
}
