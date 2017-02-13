package org.lpw.ranch.recycle;

import com.alibaba.fastjson.JSONObject;

/**
 * 回收站操作支持类。
 *
 * @author lpw
 */
public interface RecycleHelper {
    /**
     * 删除到回收站。
     *
     * @param modelClass Model类。
     * @param id         ID值。
     * @param <T>        Model类。
     */
    <T extends RecycleModel> void delete(Class<T> modelClass, String id);

    /**
     * 从回收站还原。
     *
     * @param modelClass Model类。
     * @param id         ID值。
     * @param <T>        Model类。
     */
    <T extends RecycleModel> void restore(Class<T> modelClass, String id);

    /**
     * 检索回收站中的数据集。
     *
     * @param modelClass Model类。
     * @param <T>        Model类。
     * @return 回收站中的数据集。
     */
    <T extends RecycleModel> JSONObject recycle(Class<T> modelClass);
}
