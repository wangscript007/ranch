package org.lpw.ranch.editor;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author lpw
 */
interface EditorDao {
    PageList<EditorModel> query(Set<String> ids, int template, String type, String name, String label, String group, int price, int vipPrice,
                                int limitedPrice, Timestamp limitedTime, int modified, Set<Integer> states, Timestamp createStart,
                                Timestamp createEnd, Timestamp modifyStart, Timestamp modifyEnd, Order order, int pageSize, int pageNum);

    PageList<EditorModel> query(int template, String type, int state, int pageSize, int pageNum);

    PageList<EditorModel> query(Timestamp[] modify);

    EditorModel findById(String id);

    EditorModel findByGroup(String group);

    void save(EditorModel editor);

    void price(String[] ids, String type, String group, int price, int vipPrice, int limitedPrice, Timestamp limitedTime);

    void group(String type, String oldGroup, String newGroup);

    void sort(String id, String type, int sort);

    void download(String id, int count);
}
