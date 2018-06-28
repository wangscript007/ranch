package org.lpw.ranch.editor.role;

import org.lpw.tephra.dao.orm.PageList;

import java.util.Set;

/**
 * @author lpw
 */
interface RoleDao {
    PageList<RoleModel> query(String user, int template, String etype, Set<Integer> states, int pageSize, int pageNum);

    PageList<RoleModel> query(String editor, boolean sortable);

    PageList<RoleModel> query(Set<String> users);

    RoleModel findById(String id);

    RoleModel find(String user, String editor);

    void save(RoleModel role);

    void delete(RoleModel role);
}
