package org.lpw.ranch.recycle;

/**
 * @author lpw
 */
public interface RecycleTester {
    /**
     * 测试回收站功能。
     *
     * @param testerDao  测试Dao。
     * @param name       Model NAME值。
     * @param uriPrefix  URI前缀。
     * @param codePrefix 错误编码前缀。
     * @param <T>        Model类。
     */
    <T extends RecycleModel> void all(RecycleTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix);

    /**
     * 测试删除到回收站功能。
     *
     * @param testerDao  测试Dao。
     * @param name       Model NAME值。
     * @param uriPrefix  URI前缀。
     * @param codePrefix 错误编码前缀。
     * @param <T>        Model类。
     */
    <T extends RecycleModel> void delete(RecycleTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix);

    /**
     * 测试从回收站还原功能。
     *
     * @param testerDao  测试Dao。
     * @param name       Model NAME值。
     * @param uriPrefix  URI前缀。
     * @param codePrefix 错误编码前缀。
     * @param <T>        Model类。
     */
    <T extends RecycleModel> void restore(RecycleTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix);

    /**
     * 测试检索回收站数据功能。
     *
     * @param testerDao 测试Dao。
     * @param name      Model NAME值。
     * @param uriPrefix URI前缀。
     * @param <T>       Model类。
     */
    <T extends RecycleModel> void recycle(RecycleTesterDao<T> testerDao, String name, String uriPrefix);
}
