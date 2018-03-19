package org.lpw.ranch.editor.role;

/**
 * @author lpw
 */
interface RoleDao {
    RoleModel find(String user, String editor);

    void save(RoleModel role);
}
