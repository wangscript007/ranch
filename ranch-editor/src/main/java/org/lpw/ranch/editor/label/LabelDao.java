package org.lpw.ranch.editor.label;

import org.lpw.tephra.dao.orm.PageList;

import java.util.Set;

/**
 * @author lpw
 */
interface LabelDao {
    PageList<LabelModel> query(String name);

    PageList<LabelModel> query(Set<String> editors);

    void save(LabelModel label);

    void rename(String oldName, String newName);

    void delete(String editor);

    void close();
}
