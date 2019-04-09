package org.lpw.ranch.editor;

import org.lpw.ranch.editor.element.ElementModel;

import java.util.List;

/**
 * 编辑器发布监听器。
 *
 * @author lpw
 */
public interface EditorPublishListener {
    /**
     * 发布。
     *
     * @param sid      Session ID。
     * @param editor   编辑器。
     * @param elements 根元素集。
     */
    void publish(String sid, EditorModel editor, List<ElementModel> elements);
}
