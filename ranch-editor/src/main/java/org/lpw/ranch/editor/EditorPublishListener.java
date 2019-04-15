package org.lpw.ranch.editor;

import org.lpw.ranch.editor.element.ElementModel;

import java.util.List;
import java.util.Set;

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
     * @param types  发布类型集，为空表示全部。
     * @param editor   编辑器。
     * @param elements 根元素集。
     */
    void publish(String sid, Set<String> types, EditorModel editor, List<ElementModel> elements);
}
