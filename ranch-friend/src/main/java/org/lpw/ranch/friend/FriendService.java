package org.lpw.ranch.friend;

import com.alibaba.fastjson.JSONArray;

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
     * 添加好友。
     *
     * @param friend 好友ID或唯一编码。
     * @param memo   请求备注。
     */
    void create(String friend, String memo);

    /**
     * 通过好友审核。
     *
     * @param friend 好友ID。
     * @param memo   备注。
     */
    void pass(String friend, String memo);

    /**
     * 设置好友备注。
     *
     * @param friend 好友ID。
     * @param memo   备注。
     */
    void memo(String friend, String memo);

    /**
     * 查找好友信息。
     *
     * @param friend 好友ID。
     * @return 好友信息；如果不存在则返回null。
     */
    FriendModel find(String friend);
}
