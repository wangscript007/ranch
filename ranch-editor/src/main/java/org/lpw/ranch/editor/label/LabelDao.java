package org.lpw.ranch.editor.label;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface LabelDao {
    PageList<LabelModel> query(String name);

    void save(LabelModel label);

    void delete(String editor);
}
