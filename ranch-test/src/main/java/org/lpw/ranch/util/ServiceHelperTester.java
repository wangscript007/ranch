package org.lpw.ranch.util;

/**
 * @author lpw
 */
public interface ServiceHelperTester {
    /**
     * 测试get服务。
     *
     * @param serviceHelper 服务支持实例。
     * @param key           服务key。
     * @param <T>           服务支持类。
     */
    <T extends ServiceHelperSupport> void get(T serviceHelper, String key);

    /**
     * 测试填充服务。
     *
     * @param serviceHelper 服务支持实例。
     * @param key           服务key。
     * @param <T>           服务支持类。
     */
    <T extends ServiceHelperSupport> void fill(T serviceHelper, String key);
}
