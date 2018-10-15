package org.lpw.ranch.editor.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorModel;
import org.lpw.tephra.dao.orm.PageList;

import java.util.Set;

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
     * 是否可创建验证器Bean名称。
     */
    String VALIDATOR_CREATABLE = RoleModel.NAME + ".validator.creatable";
    /**
     * 是否为所有者验证器Bean名称。
     */
    String VALIDATOR_OWNER = RoleModel.NAME + ".validator.owner";
    /**
     * 是否可编辑验证器Bean名称。
     */
    String VALIDATOR_EDITABLE = RoleModel.NAME + ".validator.editable";
    /**
     * 是否可删除验证器Bean名称。
     */
    String VALIDATOR_DELETABLE = RoleModel.NAME + ".validator.deletable";
    /**
     * 是否可浏览验证器Bean名称。
     */
    String VALIDATOR_VIEWABLE = RoleModel.NAME + ".validator.viewable";
    /**
     * 是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = RoleModel.NAME + ".validator.exists";

    /**
     * 检索用户角色信息集。
     *
     * @param user     用户。
     * @param template 编辑器模板。
     * @param etype    编辑器类型。
     * @param states   编辑器状态集。
     * @return 角色信息集。
     */
    PageList<RoleModel> query(String user, int template, String etype, Set<Integer> states);

    /**
     * 检索角色集。
     *
     * @param editor 编辑器。
     * @return 角色集。
     */
    JSONArray query(String editor);

    /**
     * 查找。
     *
     * @param id ID值。
     * @return 角色信息；不存在则返回null。
     */
    RoleModel findById(String id);

    /**
     * 查找。
     *
     * @param user   用户。
     * @param editor 编辑器。
     * @return 角色信息；不存在则返回null。
     */
    RoleModel find(String user, String editor);

    /**
     * 检索用户的编辑器ID集。
     *
     * @param users 用户ID集。
     * @return 编辑器ID集。
     */
    Set<String> editors(Set<String> users);

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
     * 统计创建数。
     *
     * @return 创建数。
     */
    JSONObject countOwner();

    /**
     * 保存角色。
     *
     * @param user   用户。
     * @param editor 编辑器。
     * @param type   类型。
     */
    void save(String user, String editor, Type type);

    /**
     * 更新编辑器信息。
     *
     * @param editor 编辑器。
     */
    void modify(EditorModel editor);

    /**
     * 创建分享。
     *
     * @param editor   编辑器。
     * @param password 访问密码。
     * @return 角色信息。
     */
    JSONObject share(String editor, String password);

    /**
     * 设置访问密码。
     *
     * @param id       ID值。
     * @param password 访问密码。
     */
    void password(String id, String password);

    /**
     * 删除角色。
     *
     * @param user   用户。
     * @param editor 编辑器。
     */
    void delete(String user, String editor);

    /**
     * 从回收站移除。
     *
     * @param editor 编辑器。
     */
    void remove(String editor);
}
