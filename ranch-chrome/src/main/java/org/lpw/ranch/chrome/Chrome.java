package org.lpw.ranch.chrome;

/**
 * Chrome处理器。
 *
 * @author lpw
 */
public interface Chrome {
    /**
     * 类型。
     */
    enum Type {
        Pdf,
        Img
    }

    /**
     * 获取处理器类型。
     *
     * @return 处理器类型。
     */
    Type getType();

    /**
     * 执行Chrome操作。
     *
     * @param chrome 参数。
     * @param url    请求URL地址。
     * @return 执行结果。
     */
    byte[] execute(ChromeModel chrome, String url);
}
