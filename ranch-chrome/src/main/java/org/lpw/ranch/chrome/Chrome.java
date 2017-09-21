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
     * 设置Chrome服务地址及端口号。
     *
     * @param host 服务器地址。
     * @param port 端口号。
     * @return 当前Chrome实例。
     */
    Chrome set(String host, int port);

    /**
     * 执行Chrome操作。
     *
     * @param chrome 参数。
     * @param url    请求URL地址。
     * @return 执行结果。
     */
    byte[] execute(ChromeModel chrome, String url);
}
