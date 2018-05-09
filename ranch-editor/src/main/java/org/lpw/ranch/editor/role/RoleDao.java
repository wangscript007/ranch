package org.lpw.ranch.editor.role;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface RoleDao {
    PageList<RoleModel> query(String user, int pageSize, int pageNum);

    PageList<RoleModel> query(String editor);

    RoleModel find(String user, String editor);

    void save(RoleModel role);

    void delete(RoleModel role);
}
