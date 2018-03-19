package org.lpw.ranch.editor.role;

/**
 * @author lpw
 */
public interface RoleService {
    enum Type {
        /**
         * 所有者。
         */
        Owner,
        /**
         * 可编辑。
         */
        Editor,
        /**
         * 仅浏览。
         */
        Viewer
    }

    /**
     * 是否为所有者验证器Bean名称。
     */
    String VALIDATOR_OWNER = RoleModel.NAME + ".validator.owner";
    /**
     * 是否可编辑验证器Bean名称。
     */
    String VALIDATOR_EDITABLE = RoleModel.NAME + ".validator.editable";
    /**
     * 是否可浏览验证器Bean名称。
     */
    String VALIDATOR_VIEWABLE = RoleModel.NAME + ".validator.viewable";

    /**
     * 查找。
     *
     * @param user   用户。
     * @param editor 编辑器。
     * @return 角色信息；不存在则返回null。
     */
    RoleModel find(String user, String editor);

    /**
     * 验证用户是否拥有操作权限。
     *
     * @param user   用户，空则默认为当前用户。
     * @param editor 编辑器。
     * @param type   类型。
     * @return 如果包含则返回true；否则返回false。
     */
    boolean hasType(String user, String editor, Type type);

    /**
     * 保存角色。
     *
     * @param user   用户。
     * @param editor 编辑器。
     * @param type   类型。
     */
    void save(String user, String editor, Type type);
}
