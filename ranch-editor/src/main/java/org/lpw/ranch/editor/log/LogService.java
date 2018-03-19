package org.lpw.ranch.editor.log;

import org.lpw.ranch.editor.element.ElementModel;

/**
 * @author lpw
 */
public interface LogService {
    /**
     * 操作。
     */
    enum Operation {
        /**
         * 新增。
         */
        Create,
        /**
         * 修改。
         */
        Modify,
        /**
         * 删除。
         */
        Delete
    }

    /**
     * 保存日志。
     *
     * @param element   元素信息。
     * @param operation 操作。
     */
    void save(ElementModel element, Operation operation);
}
