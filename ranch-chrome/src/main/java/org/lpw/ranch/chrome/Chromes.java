package org.lpw.ranch.chrome;

/**
 * Chrome处理器集。
 *
 * @author lpw
 */
public interface Chromes {
    /**
     * 获取Chrome处理器。
     *
     * @param type 处理器类型。
     * @return Chrome处理器。
     */
    Chrome get(Chrome.Type type);
}
