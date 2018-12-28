package org.lpw.ranch.editor.element;

import org.lpw.tephra.dao.orm.PageList;

import java.util.Map;

/**
 * @author lpw
 */
interface ElementDao {
    PageList<ElementModel> query(String editor, String parent);

    ElementModel findById(String id, String editor);

    Map<String, Long> modify(long modify);

    void save(ElementModel element);

    void delete(ElementModel element);
}
