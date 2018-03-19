package org.lpw.ranch.editor;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface EditorDao {
    EditorModel findById(String id);

    void save(EditorModel editor);
}
