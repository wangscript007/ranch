package org.lpw.ranch.editor;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author lpw
 */
interface EditorDao {
    PageList<EditorModel> query(Set<String> ids, int template, String type, String name, String keyword, int state,
                                Timestamp createStart, Timestamp createEnd, Timestamp modifyStart, Timestamp modifyEnd,
                                int pageSize, int pageNum);

    EditorModel findById(String id);

    void save(EditorModel editor);
}
