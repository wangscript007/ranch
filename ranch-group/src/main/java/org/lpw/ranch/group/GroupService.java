package org.lpw.ranch.group;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface GroupService {
    /**
     * 是否可修改验证器Bean名称。
     */
    String VALIDATOR_MODIFY_ENABLE = GroupModel.NAME + ".validator.modify-enable";
    /**
     * 是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = GroupModel.NAME + ".validator.exists";

    /**
     * 检索用户所在群组集。
     *
     * @param user 用户ID。
     * @return 群组集；如果未找到则返回空JSON数据。
     */
    JSONArray queryByUser(String user);

    /**
     * 创建新群组。
     *
     * @param name  名称。
     * @param note  公告。
     * @param audit 新成员是否需要审核：0-否；1-是。
     * @return 新群组信息。
     */
    JSONObject create(String name, String note, int audit);

    /**
     * 修改群组名称。
     *
     * @param id   ID值。
     * @param name 名称。
     * @return 群组信息。
     */
    JSONObject name(String id, String name);

    /**
     * 修改群组公告。
     *
     * @param id   ID值。
     * @param note 公告。
     * @return 群组信息。
     */
    JSONObject note(String id, String note);

    /**
     * 修改群组审核设置。
     *
     * @param id    ID值。
     * @param audit 新成员是否需要审核：0-否；1-是。
     * @return 群组信息。
     */
    JSONObject audit(String id, int audit);

    /**
     * 增减成员数。
     *
     * @param id    群组ID值。
     * @param count 成员数：正数为增加，负数为减少。
     */
    void member(String id, int count);

    /**
     * 获取群组信息。
     *
     * @param id ID值。
     * @return 群组信息，如果不存在则返回空JSON数据。
     */
    JSONObject get(String id);
}
