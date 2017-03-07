package org.lpw.ranch.friend;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface FriendService {
    /**
     * 好友信息是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = FriendModel.NAME + ".validator.exists";

    /**
     * 检索当前用户好友列表。
     *
     * @param state 状态：0-待对方确认；1-待己方确认；2-已通过；3-已拒绝/拉黑。
     * @return 当前用户好友列表；如果未找到则返回空集。
     */
    JSONArray query(int state);

    /**
     * 获取好友信息。
     *
     * @param user 好友ID值。
     * @return 好友信息；如果不存在或未审核通过则返回空JSON。
     */
    JSONObject findAsJson(String user);

    /**
     * 添加好友。
     *
     * @param user 好友ID。
     * @param memo 请求备注。
     */
    void create(String user, String memo);

    /**
     * 通过好友审核。
     *
     * @param user 好友ID。
     * @param memo 备注。
     */
    void pass(String user, String memo);

    /**
     * 设置好友备注。
     *
     * @param user 好友ID。
     * @param memo 备注。
     */
    void memo(String user, String memo);

    /**
     * 查找好友信息。
     *
     * @param user 好友ID。
     * @return 好友信息；如果不存在则返回null。
     */
    FriendModel find(String user);
}
