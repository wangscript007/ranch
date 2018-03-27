package org.lpw.ranch.editor;

/**
 * @author lpw
 */
interface EditorDao {
    EditorModel findById(String id);

    void save(EditorModel editor);
}
