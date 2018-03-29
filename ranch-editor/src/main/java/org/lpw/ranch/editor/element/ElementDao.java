package org.lpw.ranch.editor.element;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author lpw
 */
interface ElementDao {
    PageList<ElementModel> query(String editor, String parent);

    ElementModel findById(String id);

    Map<String, Long> modify(long modify);

    void save(ElementModel element);

    void delete(ElementModel element);
}
