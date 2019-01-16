package org.lpw.ranch.log;

/**
 * @author lpw
 */
public interface LogService {
    /**
     * 保存日志。
     *
     * @param type 类型。
     */
    void save(String type);
}
